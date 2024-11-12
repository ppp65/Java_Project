package com.example.project.controller;

import com.example.project.MatchDto;
import com.example.project.NewsDto;
import com.example.project.Schedule;
import com.example.project.SkySportsEplCrawlerWithMore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.project.service.DBService;
import java.util.List;

@RestController @RequestMapping("/api/DBUpload")
public class DBController {

    @Autowired
    private DBService DBService;

    @PostMapping("/uploadNews")
    public String uploadNewsData() {
        SkySportsEplCrawlerWithMore skySportsEplCrawlerWithMore = new SkySportsEplCrawlerWithMore();
        List<NewsDto> crawledNewsData = skySportsEplCrawlerWithMore.executeCrawling();
        DBService.uploadNewsDataToOracle(crawledNewsData);
        return "뉴스 데이터 업로딩";
    }

    @PostMapping("/uploadMatch")
    public String uploadMatchData() {
        Schedule schedule = new Schedule();
        List<MatchDto> crawledMatchData = schedule.executeCrawling();
        DBService.uploadMatchDataToOracle(crawledMatchData);
        return "경기 일정 업로딩";
    }
}
