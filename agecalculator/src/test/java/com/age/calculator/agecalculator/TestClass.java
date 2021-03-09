package com.age.calculator.agecalculator;

import java.time.Period;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TestClass {

    DateUtil u;

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "5|may",
        "05|may",
        "may|may",
        "aug|august",
        "jan|january",
        "dec|december",
        "12|december",
        "7|july",
        "07|july",
        "jun|june"
    })
    public void monthName(String input, String expectedOutput) {
        String actualOutput = u.getMonthName(input);
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "december|Dec",
        "may|May",
        "june|Jun",
        "AUGUST|Aug"
    })
    public void shortenMonth(String input, String expectedOutput) {
        String actualOutput = u.shortenMonth(input);
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "31/may/1989|31 May 1989",
        "31-05-1989|31 May 1989",
        "31-5-1989|31 May 1989",
        "31 05 1989|31 May 1989",
        "31 MAY 1989|31 May 1989",
        "31 May 1989|31 May 1989",
        "1 may 1989|01 May 1989"
    })
    public void standardizeDate(String input, String expectedOutput) {
        String actualOutput = u.standardizeDate(input);
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @ParameterizedTest
    @ValueSource(strings = {"31 may 1989", "31-5-1989", "31/MAY/1989", "31/05/1989"})
    public void calculateAge(String input) {
        Age a = u.calculateAge(input);
        Assertions.assertTrue(a.getYears() >= 31);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "1/may/1989|1 dec 1989",
        "1-05-1989|31 jun 1989",
        "5-5-1989|31 aug 1989"
    })
    public void calculateAgeBetweenDates(String input1, String input2) {
        Age a = u.calculateAge(input1, input2);
        Assertions.assertTrue(a.getMonths() >= 1);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "0,0,30|0,0,30",
        "0,0,20|0,0,10",
        "0,0,20|0,0,20",
        "0,8,0|0,4,0",
        "0,9,27|0,4,2",
        "0,9,27|0,4,3",
        "0,9,27|0,4,4"
    })
    public void testAgeAdditions(String input1, String input2) {
        String[] inputs1 = input1.split(",");
        String[] inputs2 = input2.split(",");
        Age a1 = new Age(Period.of(Integer.parseInt(inputs1[0]), Integer.parseInt(inputs1[1]), Integer.parseInt(inputs1[2])));
        Age a2 = new Age(Period.of(Integer.parseInt(inputs2[0]), Integer.parseInt(inputs2[1]), Integer.parseInt(inputs2[2])));
        Age total = a1.add(a2);
        p("(" + a1 + " | " + a2 + ") -> total age : " + total);
    }

    static void p(Object o) {
        System.out.print("\n" + o + "\n");
    }
}
