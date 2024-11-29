package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScheduleController {

    @Autowired
    private MatchDataService matchDataService;

    @GetMapping("/api/getMatchesForMonth")
    public List<String[]> getMatchesForMonth(@RequestParam int year, @RequestParam int month) {
        List<String[]> match;
        match = matchDataService.getMatchesForMonth(year, month);
        // 데이터가 없으면 null 반환
        if (match == null || match.isEmpty()) {
            return null;
        }
        return match;
    }
}