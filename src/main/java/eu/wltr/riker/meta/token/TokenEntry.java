package eu.wltr.riker.meta.token;


import eu.wltr.riker.meta.MetaEntry;


public class TokenEntry extends MetaEntry {

	private Token value;

	public TokenEntry() {

	}

	public TokenEntry(Token value) {
		this.value = value;

	}

	public Token getValue() {
		return value;
	}

	public void setValue(Token value) {
		this.value = value;
	}

}
