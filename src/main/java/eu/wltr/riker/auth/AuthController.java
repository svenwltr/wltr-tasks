package eu.wltr.riker.auth;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.openidconnect.client.response.OpenIdConnectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.wltr.riker.auth.bo.AuthBo;
import eu.wltr.riker.auth.pojo.Login.Provider;
import eu.wltr.riker.auth.pojo.OAuthCredentials;
import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;
import eu.wltr.riker.meta.MetaBo;


@Controller
@Scope("request")
@RequestMapping("login/oauth/")
public class AuthController {

	@Autowired
	private AuthBo bo;

	@Autowired
	private MetaBo metaBo;

	@Autowired
	private HttpServletRequest request;

	private String redirect(String uri) {
		return String.format("redirect:%s", uri);

	}

	private StringBuilder getBaseUri() {
		return new StringBuilder()
				.append(request.getScheme())
				.append("://")
				.append(request.getServerName())
				.append(":")
				.append(request.getServerPort())
				.append("/");

	}

	private OAuthCredentials.Provider getProvider(String name) {
		OAuthCredentials all = metaBo.get(OAuthCredentials.class);
		return all.getProviders().get(name);

	}

	private String getSuccessUri() {
		return getBaseUri().toString();

	}

	private StringBuilder getControllerUri() {
		return getBaseUri().append("api/login/oauth/");
	}

	private String getAuthorizeUri(String provider) {
		return getControllerUri().append("authorize/")
				.append(provider).append("/").toString();

	}

	private String getCallbackUri(String provider) {
		return getControllerUri().append("callback/")
				.append(provider).append("/").toString();

	}

	@RequestMapping("/{providerName}/")
	public String index(
			@PathVariable String providerName,
			@CookieValue(value = "session_id", required = false) String sid,
			@CookieValue(value = "session_secret", required = false) String secret) {

		User user = bo.getUserBySession(sid);
		Session session = bo.getSession(user, sid);

		if (bo.verifySession(session, secret))
			return redirect(getSuccessUri());

		else
			return redirect(getAuthorizeUri(providerName));

	}

	@RequestMapping("/authorize/{providerName}/")
	public String getAuthorize(@PathVariable String providerName)
			throws OAuthSystemException {
		OAuthCredentials.Provider provider = getProvider(providerName);

		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(provider.AuthorizationEndpoint)
				.setClientId(provider.clientId)
				.setRedirectURI(getCallbackUri(providerName))
				.setResponseType(ResponseType.CODE.toString())
				.setScope("openid")
				// .setState("abc") // TODO
				.buildQueryMessage();

		return redirect(request.getLocationUri());

	}

	@RequestMapping("/callback/{providerName}/")
	public String getCallback(
			@PathVariable String providerName,
			HttpServletRequest request,
			HttpServletResponse response)
			throws OAuthProblemException, OAuthSystemException {
		OAuthCredentials.Provider provider = getProvider(providerName);

		OAuthAuthzResponse oar = OAuthAuthzResponse
				.oauthCodeAuthzResponse(request);
		OAuthClientRequest crequest = OAuthClientRequest
				.tokenLocation(provider.tokenEndpoint)
				.setClientId(provider.clientId)
				.setClientSecret(provider.clientSecret)
				.setRedirectURI(getCallbackUri(providerName))
				.setCode(oar.getCode())
				.setGrantType(GrantType.AUTHORIZATION_CODE)
				.buildBodyMessage();
		OAuthClient client = new OAuthClient(new URLConnectionClient());
		OAuthAccessTokenResponse oauthResponse = client.accessToken(crequest,
				OpenIdConnectResponse.class);
		OpenIdConnectResponse openIdConnectResponse = ((OpenIdConnectResponse) oauthResponse);

		JWT idToken = openIdConnectResponse.getIdToken();
		String subject = idToken.getClaimsSet().getSubject();

		User user = bo.getUserByLogin(Provider.Google, subject);

		if (user == null) {
			user = bo.createUser();
			bo.addLogin(user, Provider.Google, subject);

		}

		String secret = bo.generateSessionSecret();
		Session session = bo.createSession(user, secret);

		response.addCookie(createCookie("session_id", session.getId()));
		response.addCookie(createCookie("session_secret", secret));

		return redirect(getSuccessUri());

	}

	private Cookie createCookie(String name, String value) {
		Cookie c = new Cookie(name, value);
		c.setPath("/");
		c.setMaxAge(3600);

		return c;

	}

}
