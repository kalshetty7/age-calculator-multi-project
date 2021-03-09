package com.age.calculator.agecalculator;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    static final String PATTERN = "dd MMM yyyy";
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public static String getMonthName(String m) {
        int monthNo = 0;
        String monthName = "";
        try {
            monthNo = Integer.parseInt(m);
            for (Month month : Month.values()) {
                if (month.getValue() == monthNo) {
                    monthName = month.name();
                    break;
                }
            }
        } catch (Exception e) {
            for (Month month : Month.values()) {
                if (month.name().toLowerCase().contains(m.toLowerCase())) {
                    monthName = month.name();
                    break;
                }
            }
        }
        return monthName.toLowerCase();
    }

    public static String shortenMonth(String m) {
        return (m.charAt(0) + "").toUpperCase() + m.substring(1, 3).toLowerCase();
    }

    public static String standardizeDate(String m) {
        String delim = "", possibleDelims = "\\/- ";
        String delims[] = possibleDelims.split("");
        for (String d : delims) {
            if (m.contains(d)) {
                delim = d;
                break;
            }
        }
        String tokens[] = m.split(delim);
        tokens[1] = shortenMonth(getMonthName(tokens[1]));
        if (tokens[0].length() == 1) {
            tokens[0] = "0" + tokens[0];
        }
        String output = tokens[0] + " " + tokens[1] + " " + tokens[2];
        return output;
    }
    
    public static String today(){
        return LocalDate.now().format(DATE_FORMATTER);
    }

    public static LocalDate toLocalDate(String input) {
        input=input.trim();
        String inputDate = standardizeDate(input);
        return LocalDate.parse(inputDate, DATE_FORMATTER);
    }

    public static Age calculateAge(String input) {
        return new Age(toLocalDate(input));
    }

    public static Age calculateAge(String input1, String input2) {
        return new Age(toLocalDate(input1), toLocalDate(input2));
    }
}
