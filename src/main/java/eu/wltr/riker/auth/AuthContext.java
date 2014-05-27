package eu.wltr.riker.auth;


import eu.wltr.riker.auth.pojo.Session;
import eu.wltr.riker.auth.pojo.User;



public class AuthContext {

	private final User user;

	private final Session session;

	public AuthContext(User user, Session session) {
		this.user = user;
		this.session = session;

	}

	public User getUser() {
		return user;

	}

	public Session getSession() {
		return session;

	}

}
