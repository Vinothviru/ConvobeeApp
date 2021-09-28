package com.convobee.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
}
