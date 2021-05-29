package com.reports.frequency_reports;

import com.reports.frequency_reports.utility.Util;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class FrequencyReportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrequencyReportsApplication.class, args);
        Util util = new Util();
        util.generateReportTest();
    }

}
