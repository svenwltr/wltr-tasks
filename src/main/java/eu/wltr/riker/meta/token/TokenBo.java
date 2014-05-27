package eu.wltr.riker.meta.token;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.wltr.riker.meta.MetaDto;


@Service
public class TokenBo {

	@Autowired
	private MetaDto metaDto;

	public Token next() {
		TokenEntry entry = metaDto.get(TokenEntry.class);

		if (entry == null)
			entry = new TokenEntry(new Token("foo"));

		entry.getValue().increment();
		metaDto.update(entry);

		return entry.getValue();

	}

}
