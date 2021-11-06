package com.convobee.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
	public static LocalDateTime backToLocal(LocalDateTime utc) {
		final LocalDateTime backToLocal = DateTimeUtil.toZone(utc,ZoneOffset.UTC,ZoneId.systemDefault()); 
		System.out.println("Back To Local: " + backToLocal);
		return backToLocal;
	}

	public static LocalDateTime toZone(final LocalDateTime time, final ZoneId fromZone, final ZoneId toZone) {
		final ZonedDateTime zonedtime = time.atZone(fromZone);
		final ZonedDateTime converted = zonedtime.withZoneSameInstant(toZone);
		return converted.toLocalDateTime();
	}

	public static LocalDateTime toZone(final LocalDateTime time, final ZoneId toZone) {
		return DateTimeUtil.toZone(time, ZoneId.systemDefault(), toZone);
	}

	public static LocalDateTime toUtc(final LocalDateTime time, final ZoneId fromZone) {
		return DateTimeUtil.toZone(time, fromZone, ZoneOffset.UTC);
	}

	public static LocalDateTime toUtc(final LocalDateTime time, String tz) {
		return DateTimeUtil.toUtc(time, ZoneId.of(tz));
	}
	
	/* Getting current UTC time to store created_at and modified_at fields in table */
	public static Timestamp getCurrentUTCTime() {
		String utcDateTime = LocalDateTime.now(ZoneId.of("UTC"))
		  .truncatedTo(ChronoUnit.SECONDS)
		  .format(DateTimeFormatter.ISO_DATE_TIME).toString();//Used to fetch current UTC time
		return Timestamp.valueOf(utcDateTime.toString().replace('T', ' '));
	}
}
