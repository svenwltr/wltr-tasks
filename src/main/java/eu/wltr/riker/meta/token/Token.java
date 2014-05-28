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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (obj instanceof String)
			return token.equals(obj);

		if (!(obj instanceof Token))
			return false;

		Token other = (Token) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;

	}

}
