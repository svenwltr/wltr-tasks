package eu.wltr.riker.auth.pojo;

import java.util.ArrayList;
import java.util.List;

import org.jongo.marshall.jackson.oid.Id;

import eu.wltr.riker.meta.token.Token;


public class User {

	@Id
	private Token token;

	private String name;

	private List<Session> sessions = new ArrayList<>();

	private List<Login> logins = new ArrayList<>();

	public Token getToken() {
		return token;
	
	}

	public void setToken(Token token) {
		this.token = token;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Session> getSessions() {
		return sessions;

	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;

	}

	public List<Login> getLogins() {
		return logins;

	}

	public void setLogins(List<Login> logins) {
		this.logins = logins;

	}

}
