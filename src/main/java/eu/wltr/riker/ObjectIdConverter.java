package eu.wltr.riker;


import java.math.BigInteger;

import org.bson.types.ObjectId;


public class ObjectIdConverter {

	private static char intToChar(int i) {
		if (i < 36)
			return Character.forDigit(i, 36);
		else
			return Character.toUpperCase(Character.forDigit(i - 26, 36));
	}

	private static int charToInt(char c) {
		int i = 0;
	
		if (Character.isUpperCase(c))
			i += 26;
	
		return i + Character.getNumericValue(c);
	
	}

	public static String integerToString(BigInteger value) {
		StringBuilder s = new StringBuilder();

		while (value.bitLength() > 0) {
			BigInteger[] result = value
					.divideAndRemainder(BigInteger.valueOf(62));
			value = result[0];

			char c = intToChar(result[1].intValue());
			s.insert(0, c);

		}

		return s.toString();

	}

	public static BigInteger integerFromString(String value) {
		BigInteger sol = BigInteger.ZERO;

		for (int i = 0; i < value.length(); i++) {
			int val = charToInt(value.charAt(i));
			sol = sol.multiply(BigInteger.valueOf(62));
			sol = sol.add(BigInteger.valueOf(val));

		}

		return sol;

	}

	public static String objectIdToString(ObjectId oid) {
		return integerToString(new BigInteger(oid.toByteArray()));
	
	}

	public static ObjectId objectIdFromString(String value) {
		return new ObjectId(integerFromString(value).toByteArray());

	}

}
