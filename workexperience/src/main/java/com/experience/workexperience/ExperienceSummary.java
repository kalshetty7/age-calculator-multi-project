/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experience.workexperience;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.age.calculator.agecalculator.Age;
import com.age.calculator.agecalculator.DateUtil;

/**
 *
 * @author anupkalshetty
 */
public class ExperienceSummary {

	static final String JOB_FILE_NAME = "jobs.txt";

	static final List<Job> JOB_LIST = getJobList();

	static List<Job> getJobList() {
		List<Job> jobList = new ArrayList<>();
		String dir = System.getProperty("user.dir");
		Path p = Paths.get(getJobFile(dir).getAbsolutePath());
		try {
			List<String> data = Files.readAllLines(p);
			for (String d : data) {
				String tokens[] = d.split(",");
				if (tokens[2].trim().equals("")) {
					tokens[2] = DateUtil.today();
				}
				jobList.add(new Job(tokens[0].trim(), tokens[1].trim(), tokens[2]));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jobList;
	}

	public static void main(String[] args) {
		JOB_LIST.stream().forEach(c -> {
			System.out.println(c);
		});
		p("total wk exp : " + getTotalWorkExperience());
		p("total exp : " + new Age(JOB_LIST.get(0).getStartDate(), LocalDate.now()));
	}

	static Age getTotalWorkExperience() {
		Age totalAge = new Age();
		if (CollectionUtils.isNotEmpty(JOB_LIST)) {
			for (Job j : JOB_LIST) {
				totalAge = j.getAge().add(totalAge);
			}
		}
		return totalAge;
	}

	static File getJobFile(String path) {
		File found = null;
		List<File> fileList = new ArrayList<>();
		List<File> dirList = new ArrayList<>();
		File file = new File(path);
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					fileList.add(f);
				} else {
					dirList.add(f);
				}
			}
		}

		for (File f : fileList) {
			if (f.getName().equals(JOB_FILE_NAME)) {
				found = f;
				break;
			}
		}

		if (found == null) {
			for (File f : dirList) {
				found = getJobFile(f.getAbsolutePath());
			}
		}

		return found;
	}

	static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}
}
