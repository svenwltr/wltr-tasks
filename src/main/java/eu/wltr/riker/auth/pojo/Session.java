package eu.wltr.riker.auth.pojo;


import org.joda.time.DateTime;

import eu.wltr.riker.meta.token.Token;




public class Session {

	private Token token;

	private String hashed;
	
	private DateTime expires;
	
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

	public DateTime getExpires() {
		return expires;
		
	}
	
	public void setExpires(DateTime expires) {
		this.expires = expires;
		
	}

}
