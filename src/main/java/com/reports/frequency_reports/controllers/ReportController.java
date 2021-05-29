package com.reports.frequency_reports.controllers;
import com.reports.frequency_reports.utility.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class ReportController {


    @GetMapping("/test")
    public String generateReportTest(@RequestParam(value = "sentence") String sentence, @RequestParam(value = "keyword") String keyWord) {
        log.info("from controller: ");
        log.info(sentence);
        Util utility = new Util(sentence, keyWord);
        return utility.generateReportTest();
    }
}
