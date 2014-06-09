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
import org.joda.time.DateTime;
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
public class LoginController {

	private static final String SUCCESS_REDIRECT = "redirect:/";

	@Autowired
	private AuthBo bo;

	@Autowired
	private HttpServletRequest request;

	private String redirect(String uri) {
		return String.format("redirect:%s", uri);

	}

	private String getCallbackUri(String providerName) {
		return new StringBuilder().append(request.getScheme()).append("://")
				.append(request.getServerName()).append(":")
				.append(request.getServerPort()).append("/api/login/")
				.append(providerName).append("/callback/").toString();

	}

	private OAuthProviders.Provider getProvider(String name) {
		OAuthProviders.Provider provider = bo.getProvider(name);

		if (provider == null)
			throw new Http404NotFound("Provider not found.");

		return provider;

	}

	private Cookie createCookie(String name, String value, DateTime expires) {
		Cookie c = new Cookie(name, value);
		c.setPath("/");

		long exireyMillis = expires.getMillis() - DateTime.now().getMillis();
		c.setMaxAge((int) (exireyMillis / 1000));

		return c;

	}

	private void setSessionCookies(HttpServletResponse response,
			Session session, String secret) {
		response.addCookie(createCookie("session_id", session.getToken()
				.toString(), session.getExpires()));
		response.addCookie(createCookie("session_secret", secret,
				session.getExpires()));

	}

	@RequestMapping("/")
	public String index(
			@PathVariable String providerName,
			@CookieValue(value = "session_id", required = false) String sid,
			@CookieValue(value = "session_secret", required = false) String secret)
			throws OAuthSystemException {

		if (bo.verifySession(sid, secret))
			return SUCCESS_REDIRECT;

		return redirect(generateOAuthRedirectUri(providerName));

	}

	private String generateOAuthRedirectUri(String providerName)
			throws OAuthSystemException {

		OAuthProviders.Provider provider = getProvider(providerName);

		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(provider.authorizationEndpoint)
				.setClientId(provider.clientId)
				.setRedirectURI(getCallbackUri(providerName))
				.setResponseType(ResponseType.CODE.toString())
				.setScope("openid")
				// .setState("abc") // TODO
				.buildQueryMessage();

		return request.getLocationUri();

	}

	@RequestMapping("/callback/")
	public String getCallback(@PathVariable String providerName,
			HttpServletRequest request, HttpServletResponse response)
			throws OAuthProblemException, OAuthSystemException {
		JWT token = requestOauthToken(request, providerName);
		String subject = token.getClaimsSet().getSubject();

		User user = bo.getUserByLogin(Provider.Google, subject);

		if (user == null) {
			user = bo.createUser();
			bo.addLogin(user, Provider.Google, subject);

		}

		String secret = bo.generateSessionSecret();
		Session session = bo.createSession(user, secret);

		setSessionCookies(response, session, secret);

		return SUCCESS_REDIRECT;

	}

	private JWT requestOauthToken(HttpServletRequest request,
			String providerName) throws OAuthSystemException,
			OAuthProblemException {

		OAuthProviders.Provider provider = getProvider(providerName);

		OAuthAuthzResponse oar = OAuthAuthzResponse
				.oauthCodeAuthzResponse(request);

		OAuthClientRequest crequest = OAuthClientRequest
				.tokenLocation(provider.tokenEndpoint)
				.setClientId(provider.clientId)
				.setClientSecret(provider.clientSecret)
				.setRedirectURI(getCallbackUri(providerName))
				.setCode(oar.getCode())
				.setGrantType(GrantType.AUTHORIZATION_CODE).buildBodyMessage();

		OAuthClient client = new OAuthClient(new URLConnectionClient());
		OAuthAccessTokenResponse oauthResponse = client.accessToken(crequest,
				OpenIdConnectResponse.class);
		OpenIdConnectResponse openIdConnectResponse = ((OpenIdConnectResponse) oauthResponse);

		return openIdConnectResponse.getIdToken();

	}

}
