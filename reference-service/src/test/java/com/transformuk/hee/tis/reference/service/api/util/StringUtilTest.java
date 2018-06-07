package com.transformuk.hee.tis.reference.service.api.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {
	@Test
	public void testRegex() {
		String value = "Scarborough & N E Yorks Healthcare Trust (RCC)'-";
		StringUtil.setSanitiserRegex("[^a-zA-Z0-9\\s,/\\-()]");

		Assert.assertEquals("Scarborough  N E Yorks Healthcare Trust (RCC)-", StringUtil.sanitize(value));
	}
}
