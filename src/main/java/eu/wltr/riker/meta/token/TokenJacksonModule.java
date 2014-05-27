package eu.wltr.riker.meta.token;


import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


public class TokenJacksonModule extends SimpleModule {

	private static final long serialVersionUID = -1246458220314364590L;

	@Deprecated
	@Override
	public void setupModule(SetupContext context) {
		SimpleSerializers sers = new SimpleSerializers();
		sers.addSerializer(new TokenSerializer());
		context.addSerializers(sers);

		SimpleDeserializers dess = new SimpleDeserializers();
		dess.addDeserializer(Token.class, new TokenDeserializer());
		context.addDeserializers(dess);

	}

	public static class TokenSerializer extends StdSerializer<Token> {
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

	public static class TokenDeserializer extends StdDeserializer<Token> {
		private static final long serialVersionUID = 5535862534636189872L;

		public TokenDeserializer() {
			super(Token.class);

		}

		public Token deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			return new Token(jp.getText());
		}
	}

}
