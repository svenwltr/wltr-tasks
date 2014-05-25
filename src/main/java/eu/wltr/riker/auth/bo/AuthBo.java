package eu.wltr.riker.auth.bo;


import java.math.BigInteger;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lambdaworks.crypto.SCryptUtil;

import eu.wltr.riker.ObjectIdConverter;
import eu.wltr.riker.auth.dto.UserDto;
import eu.wltr.riker.auth.pojo.Login;
import eu.wltr.riker.auth.pojo.Login.Provider;
import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;
import eu.wltr.riker.meta.MetaBo;


@Service
public class AuthBo {

	private static final int SESSION_N = 512;
	private static final int SESSION_R = 8;
	private static final int SESSION_P = 1;
	private static final int SECRET_SIZE = 1024;

	@Autowired
	private UserDto userDto;

	@Autowired
	private MetaBo metaBo;


	public Session getSession(User user, String sid) {
		if (user == null)
			return null;

		for (Session session : user.getSessions())
			if (session.getId().equals(sid))
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
		user.setName(String.format("User %s", metaBo.nextSequence().getToken()));

		userDto.save(user);

		return user;

	}

	public void addLogin(User user, Provider google, String subject) {
		Login login = new Login();
		login.setProvider(Provider.Google);
		login.setSubject(subject);

		userDto.addLogin(user.getId(), login);

	}

	public String generateSessionSecret() {
		BigInteger key = new BigInteger(SECRET_SIZE, new Random());
		String secret = ObjectIdConverter.integerToString(key);
		return secret;

	}

	public Session createSession(User user, String secret) {
		String hashed = SCryptUtil.scrypt(secret, SESSION_N, SESSION_R, SESSION_P);

		Session session = new Session();
		session.setId(metaBo.nextSequence().getToken());
		session.setHashed(hashed);

		userDto.addSession(user.getId(), session);

		return session;

	}

	public boolean verifySession(Session session, String secret) {
		return session != null && SCryptUtil.check(secret, session.getHashed());

	}

}
