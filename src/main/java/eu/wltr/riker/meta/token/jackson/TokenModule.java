package eu.wltr.riker.meta.token.jackson;


import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import eu.wltr.riker.meta.token.Token;


public class TokenModule extends SimpleModule {

	private static final long serialVersionUID = -1246458220314364590L;

	@Override
	public void setupModule(SetupContext context) {
		SimpleSerializers sers = new SimpleSerializers();
		sers.addSerializer(new TokenSerializer());
		context.addSerializers(sers);

		SimpleDeserializers dess = new SimpleDeserializers();
		dess.addDeserializer(Token.class, new TokenDeserializer());
		context.addDeserializers(dess);

	}

}
