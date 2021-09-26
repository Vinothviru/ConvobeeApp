package com.convobee.utils;

import java.util.Random;

public class SlotUtil {

	public static String getRandomSlotUrl()
	{ 
			int leftLimit = 97; // letter 'a'
			int rightLimit = 122; // letter 'z'
			int targetStringLength = 10;
			Random random = new Random();

			String generatedString = random.ints(leftLimit, rightLimit + 1)
					.limit(targetStringLength)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();
			String randomUrl = new StringBuilder().append("https://www.convobee.com/").append(generatedString).toString();
			return randomUrl;
			//System.out.println(new StringBuilder().append("https://www.convobee.com/").append(generatedString));			
	}
}
