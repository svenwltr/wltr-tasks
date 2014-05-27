package eu.wltr.riker.auth.pojo;


import eu.wltr.riker.meta.token.Token;




public class Session {

	private Token token;

	private String hashed;

	public Token getToken() {
		return token;

	}

	public void setToken(Token token) {
		this.token = token;

	}

	public String getHashed() {
		return hashed;

	}

	public void setHashed(String hashed) {
		this.hashed = hashed;

	}



}
