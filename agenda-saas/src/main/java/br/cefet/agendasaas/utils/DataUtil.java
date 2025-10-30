package br.cefet.agendasaas.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class DataUtil {

	private DataUtil() { }

	private static final DateTimeFormatter FORMAT_DATE_PT = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(new Locale("pt", "BR"));
	private static final DateTimeFormatter FORMAT_DATETIME_PT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(new Locale("pt", "BR"));

	public static LocalDate parseDatePt(String text) {
		if (text == null || text.isBlank()) return null;
		try {
			return LocalDate.parse(text.trim(), FORMAT_DATE_PT);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	public static LocalDateTime parseDateTimePt(String text) {
		if (text == null || text.isBlank()) return null;
		try {
			return LocalDateTime.parse(text.trim(), FORMAT_DATETIME_PT);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	public static String formatDatePt(LocalDate date) {
		return date == null ? "" : date.format(FORMAT_DATE_PT);
	}

	public static String formatDateTimePt(LocalDateTime dt) {
		return dt == null ? "" : dt.format(FORMAT_DATETIME_PT);
	}

	public static Timestamp toSqlTimestamp(LocalDateTime dt) {
		return dt == null ? null : Timestamp.valueOf(dt);
	}

	public static LocalDateTime fromSqlTimestamp(Timestamp t) {
		return t == null ? null : t.toLocalDateTime();
	}

	public static LocalDateTime startOfDay(LocalDate d) {
		return d == null ? null : LocalDateTime.of(d, LocalTime.MIN);
	}

	public static LocalDateTime endOfDay(LocalDate d) {
		return d == null ? null : LocalDateTime.of(d, LocalTime.MAX);
	}

}

