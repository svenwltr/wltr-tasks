package eu.wltr.riker.meta.token;


import java.math.BigInteger;

import org.bson.BasicBSONObject;

import eu.wltr.riker.ConverterUtils;


public class Token extends BasicBSONObject {

	private static final long serialVersionUID = 88737219310594927L;

	private static final String TOKEN_KEY = "token";

	public Token(String token) {
		setToken(token);

	}

	public String getToken() {
		return getString(TOKEN_KEY);
	
	}

	public void setToken(String token) {
		put(TOKEN_KEY, token);

	}

	public void increment() {
		BigInteger ordinal = ConverterUtils.integerFromString(getToken());
		ordinal = ordinal.add(BigInteger.ONE);
		setToken(ConverterUtils.integerToString(ordinal));
	
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
