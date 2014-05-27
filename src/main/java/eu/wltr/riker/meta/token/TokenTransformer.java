package eu.wltr.riker.meta.token;


import javax.annotation.PostConstruct;

import org.bson.BSON;
import org.bson.Transformer;
import org.springframework.stereotype.Service;


@Service
public class TokenTransformer {

	@PostConstruct
	public void init() {
		BSON.addEncodingHook(Token.class, new TokenEncoder());
	}

	public static class TokenEncoder implements Transformer {

		@Override
		public Object transform(Object o) {
			if (o instanceof Token) {
				Token token = (Token) o;
				return token.getToken();

			} else {
				return null;

			}

		}

	}

}
