package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private SkySportsEplCrawlerWithMore skySportsEplCrawlerWithMore;

    @GetMapping("/news")
    public String showNews(Model model) {
        List<NewsDto> newsList = skySportsEplCrawlerWithMore.getNews();
        model.addAttribute("newsList", newsList);
        return "news";
    }
}
