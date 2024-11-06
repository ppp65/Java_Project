package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ScheduleController {

    @Autowired
    private DaumEplScheduleCrawlerAllMonths daumEplScheduleCrawlerAllMonths;

    @GetMapping("/api/schedule")
    public List<MatchDto> getSchedule() {
        return daumEplScheduleCrawlerAllMonths.getSchedule();
    }
}
