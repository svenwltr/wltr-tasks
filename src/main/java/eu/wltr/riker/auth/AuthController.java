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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.wltr.riker.auth.bo.AuthBo;
import eu.wltr.riker.auth.openidconnect.OpenIdConnectResponse;
import eu.wltr.riker.auth.pojo.Login.Provider;
import eu.wltr.riker.auth.pojo.OAuthProviders;
import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;
import eu.wltr.riker.utils.httperror.Http404NotFound;


@Controller
@Scope("request")
@RequestMapping("login/{providerName}/")
public class AuthController {

	@Autowired
	private AuthBo bo;

	@Autowired
	private HttpServletRequest request;

	private String redirect(String uri) {
		return String.format("redirect:%s", uri);

	}

	private Cookie createCookie(String name, String value) {
		Cookie c = new Cookie(name, value);
		c.setPath("/");
		c.setMaxAge(3600);

		return c;

	}

	private String getSuccessUri() {
		return "/";

	}

	private String getCallbackUri(String providerName) {
		return new StringBuilder()
				.append(request.getScheme())
				.append("://")
				.append(request.getServerName())
				.append(":")
				.append(request.getServerPort())
				.append("/api/login/")
				.append(providerName)
				.append("/callback/")
				.toString();

	}

	@RequestMapping("/")
	public String index(
			@PathVariable String providerName,
			@CookieValue(value = "session_id", required = false) String sid,
			@CookieValue(value = "session_secret", required = false) String secret)
			throws OAuthSystemException {

		User user = bo.getUserBySession(sid);
		Session session = bo.getSession(user, sid);

		if (bo.verifySession(session, secret))
			return redirect(getSuccessUri());

		OAuthProviders.Provider provider = bo.getProvider(providerName);
		
		if(provider == null)
			throw new Http404NotFound("Provider not found.");

		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(provider.authorizationEndpoint)
				.setClientId(provider.clientId)
				.setRedirectURI(getCallbackUri(providerName))
				.setResponseType(ResponseType.CODE.toString())
				.setScope("openid")
				// .setState("abc") // TODO
				.buildQueryMessage();

		return redirect(request.getLocationUri());

	}

	@RequestMapping("/callback/")
	public String getCallback(
			@PathVariable String providerName,
			HttpServletRequest request,
			HttpServletResponse response)
			throws OAuthProblemException, OAuthSystemException {
		OAuthProviders.Provider provider = bo.getProvider(providerName);

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

		response.addCookie(createCookie("session_id", session.getToken()
				.toString()));
		response.addCookie(createCookie("session_secret", secret));

		return redirect(getSuccessUri());

	}

}
