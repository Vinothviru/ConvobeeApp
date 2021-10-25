package com.convobee.utils;

import java.text.DecimalFormat;
import java.util.Random;

public class CommonUtil {

	/* Generates random URL */
	public static String getRandomUrl()
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
	
	/* Calculates percentage w.r.t obtained value and total value */
	public static double calculatePercentage(double obtained, double total)
	{
		double result = (obtained/total)*100;
		DecimalFormat df = new DecimalFormat("#.##");      
		result = Double.valueOf(df.format(result));
		return result; 
	}
}
