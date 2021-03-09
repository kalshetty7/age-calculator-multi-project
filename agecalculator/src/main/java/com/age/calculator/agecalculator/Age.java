package com.age.calculator.agecalculator;

import java.time.LocalDate;
import java.time.Period;

public class Age {

    private int years, months, days;

    public Age(Period p) {
        years = p.getYears();
        months = p.getMonths();
        days = p.getDays();

        if (days >= 30) {
            //days = 35 , month=1(35/30) , days=5(35%30)
            months += days / 30;
            days = days % 30;
        }
        
        if (months >= 12) {
            //months = 17 , years=1(17/12) , days=5(17%12)
            years += months / 12;
            months = months % 12;
        }
    }

    public Age(LocalDate d) {
        this(Period.between(d, LocalDate.now()));
    }

    public Age(LocalDate d1, LocalDate d2) {
        this(Period.between(d1, d2));
    }

    public Period toPeriod() {
        return Period.of(years, months, days);
    }

    public Age add(Age a) {
        Period p = toPeriod().plus(a.toPeriod());
        return new Age(p);
    }

    @Override
    public String toString() {
        String s = "";
        if (years != 0) {
            s += years + " years";
        }
        if (months != 0) {
            if (s.length() > 0) {
                s += " , ";
            }
            s += months + " months";
        }
        if (days != 0) {
            if (s.length() > 0) {
                s += " , ";
            }
            s += days + " days";
        }
        return s;
    }

    public int getYears() {
        return years;
    }

    public int getMonths() {
        return months;
    }

    public int getDays() {
        return days;
    }
    
    public Age(){
        
    }
    

}
