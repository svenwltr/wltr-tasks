package eu.wltr.riker.auth;


import eu.wltr.riker.auth.pojo.User;



public class AuthContext {

	private final User user;

	public AuthContext(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;

	}

}
