package eu.wltr.riker.meta.token;

public class TokenMetaEntry {

	private Token token;

	public TokenMetaEntry() {
	}

	public TokenMetaEntry(String value) {
		token = new Token(value);
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

}
