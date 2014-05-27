package eu.wltr.riker.meta.token;


import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.wltr.riker.ConverterUtils;
import eu.wltr.riker.meta.MetaDto;


@Service
public class TokenBo {

	@Autowired
	private MetaDto metaDto;

	public Token next() {
		TokenMetaEntry entry = metaDto.get(TokenMetaEntry.class);

		if (entry == null || entry.getToken() == null)
			entry = new TokenMetaEntry("foo");

		increment(entry.getToken());
		metaDto.update(entry);

		return entry.getToken();

	}

	public void increment(Token token) {
		BigInteger ordinal = ConverterUtils.integerFromString(token.getToken());
		ordinal = ordinal.add(BigInteger.ONE);
		token.setToken(ConverterUtils.integerToString(ordinal));

	}

}
