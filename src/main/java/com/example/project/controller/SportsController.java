package com.example.project.controller;

import com.example.project.model.SportsTranslate;
import com.example.project.repository.SportsTranslateRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SportsController {

    private final SportsTranslateRepository sportsTranslateRepository;

    public SportsController(SportsTranslateRepository sportsTranslateRepository) {
        this.sportsTranslateRepository = sportsTranslateRepository;
    }

    @GetMapping("/translated-news")
    public String getTranslatedNews(Model model) {
        List<SportsTranslate> translatedNews = sportsTranslateRepository.findAll();
        model.addAttribute("translatedNews", translatedNews);
        return "newnews"; // templates/translated-news.html로 렌더링
    }
}
