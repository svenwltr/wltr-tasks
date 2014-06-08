package eu.wltr.riker.utils;

import java.math.BigInteger;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;


public class ConverterUtilsTest {
	
	@Test
	public void testIntegerToString() {
		String s = ConverterUtils.integerToString(new BigInteger("1337"));
		Assert.assertEquals("lz", s);
		
	}
	
	@Test
	public void testIntegerFromString() {
		BigInteger i = ConverterUtils.integerFromString("foobar");
		Assert.assertEquals(new BigInteger("14102387347"), i);
		
	}

	@Test
	public void testObjectIdToString() {
		String s = ConverterUtils.objectIdToString(new ObjectId("53945e97f01a1c177309f483"));
		Assert.assertEquals("xDIFeb0ZaIQeWy1J", s);
		
	}
	
	@Test
	public void testObjectIdFromString() {
		ObjectId o = ConverterUtils.objectIdFromString("xDIFo6q5HrJZUmDU");
		Assert.assertEquals(new ObjectId("53945eb3f01a90dce77abeb2"), o);
		
	}
	
}
