/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experience.workexperience;

import com.age.calculator.agecalculator.Age;
import static com.age.calculator.agecalculator.DateUtil.*;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author anupkalshetty
 */
@Data
public class Job {
    private String name;
    private LocalDate startDate, endDate;
    private Age age;

    public Job(String n, String s, String e) {
        name = n;
        startDate = toLocalDate(s);
        endDate = toLocalDate(e);
        age = calculateAge(s, e);
    }
}
