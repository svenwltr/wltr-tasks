package eu.wltr.riker.auth.bo;

import java.math.BigInteger;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lambdaworks.crypto.SCryptUtil;

import eu.wltr.riker.auth.dto.UserDto;
import eu.wltr.riker.auth.pojo.Login;
import eu.wltr.riker.auth.pojo.Login.Provider;
import eu.wltr.riker.auth.pojo.OAuthProviders;
import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;
import eu.wltr.riker.meta.MetaDto;
import eu.wltr.riker.meta.token.TokenBo;
import eu.wltr.riker.utils.ConverterUtils;

@Service
public class AuthBo {

	private static final int SESSION_N = 512;
	private static final int SESSION_R = 8;
	private static final int SESSION_P = 1;
	private static final int SECRET_SIZE = 1024;

	private static final Duration EXPIRY = Duration.standardDays(30);

	@Autowired
	private UserDto userDto;

	@Autowired
	private MetaDto metaDto;

	@Autowired
	private TokenBo tokenBo;

	public Session getSession(User user, String sid) {
		if (user == null)
			return null;

		for (Session session : user.getSessions())
			if (session.getToken().equals(sid))
				return session;

		return null;

	}

	public User getUserBySession(String sid) {
		return userDto.findOneBySessionId(sid);

	}

	public User getUserByLogin(Provider google, String subject) {
		return userDto.findOneByLogin(google, subject);

	}

	public User createUser() {
		User user = new User();
		user.setToken(tokenBo.next());
		user.setName(String.format("User %s", tokenBo.next()));

		userDto.save(user);

		return user;

	}

	public void addLogin(User user, Provider google, String subject) {
		Login login = new Login();
		login.setProvider(Provider.Google);
		login.setSubject(subject);

		userDto.addLogin(user.getToken(), login);

	}

	public String generateSessionSecret() {
		BigInteger key = new BigInteger(SECRET_SIZE, new Random());
		String secret = ConverterUtils.integerToString(key);
		return secret;

	}

	public Session createSession(User user, String secret) {
		String hashed = SCryptUtil.scrypt(secret, SESSION_N, SESSION_R,
				SESSION_P);

		Session session = new Session();
		session.setToken(tokenBo.next());
		session.setHashed(hashed);
		session.setExpires(DateTime.now().plus(EXPIRY));

		userDto.addSession(user.getToken(), session);

		return session;

	}

	public boolean verifySession(Session session, String secret) {
		return session != null && SCryptUtil.check(secret, session.getHashed())
				&& session.getExpires() != null
				&& session.getExpires().isAfterNow();

	}

	public boolean verifySession(String sid, String secret) {
		User user = getUserBySession(sid);
		Session session = getSession(user, sid);

		return verifySession(session, secret);
		
	}

	public OAuthProviders.Provider getProvider(String name) {
		OAuthProviders providers = metaDto.get(OAuthProviders.class);

		if (providers == null)
			return null;

		else
			return providers.getProviders().get(name);

	}

}
