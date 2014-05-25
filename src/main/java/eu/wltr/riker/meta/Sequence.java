package eu.wltr.riker.meta;


import java.math.BigInteger;

import eu.wltr.riker.ObjectIdConverter;

public class Sequence extends MetaEntry {

	private long count = 0;

	public void increment() {
		this.count++;

	}

	public long getCount() {
		return count;

	}

	public String getToken() {
		return ObjectIdConverter.integerToString(new BigInteger(String
				.valueOf(count)));

	}

}
