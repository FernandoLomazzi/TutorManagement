package model.dao.sqlite;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParserSQLite {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static LocalDate parseString(String dateString) {
    	return LocalDate.parse(dateString, formatter);
    }
    public static String parseDate(LocalDate date) {
    	return date.toString();
    }
}
