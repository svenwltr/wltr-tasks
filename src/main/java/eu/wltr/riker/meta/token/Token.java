package eu.wltr.riker.meta.token;




public class Token {

	private String token;

	public Token(String token) {
		setToken(token);

	}

	public String getToken() {
		return token;
	
	}

	public void setToken(String token) {
		this.token = token;

	}

	@Override
	public String toString() {
		return getToken();

	}

	@Override
	public boolean equals(Object o) {
		if (super.equals(o))
			return true;

		return getToken().equals(o);

	}

}
