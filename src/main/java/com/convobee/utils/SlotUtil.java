package com.convobee.utils;

import java.util.LinkedList;
import java.util.List;
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
	
	public static List<String> getSlotTimings(int minuteDiff)
	{
		List<String> timings = new LinkedList<String>();
		if(minuteDiff==0)
		{
			timings.add("7 AM");
			timings.add("8 AM");
			timings.add("9 AM");
			timings.add("10 AM");
			timings.add("5 PM");
			timings.add("6 PM");
			timings.add("7 PM");
			timings.add("8 PM");
			timings.add("9 PM");
		}
		else {
			timings.add("7:30 AM");
			timings.add("8:30 AM");
			timings.add("9:30 AM");
			timings.add("10:30 AM");
			timings.add("5:30 PM");
			timings.add("6:30 PM");
			timings.add("7:30 PM");
			timings.add("8:30 PM");
			timings.add("9:30 PM");
		}
		return timings;
	}
}
