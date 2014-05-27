package eu.wltr.riker.meta.token.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import eu.wltr.riker.meta.token.Token;

public class TokenDeserializer extends StdDeserializer<Token> {
	private static final long serialVersionUID = 5535862534636189872L;

	public TokenDeserializer() {
		super(Token.class);

	}

	public Token deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return new Token(jp.getText());
	}
}