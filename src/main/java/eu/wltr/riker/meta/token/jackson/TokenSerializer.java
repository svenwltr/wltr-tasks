package eu.wltr.riker.meta.token.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import eu.wltr.riker.meta.token.Token;

public class TokenSerializer extends StdSerializer<Token> {
	public TokenSerializer() {
		super(Token.class);

	}

	@Override
	public void serialize(Token value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(value.toString());
	}
}