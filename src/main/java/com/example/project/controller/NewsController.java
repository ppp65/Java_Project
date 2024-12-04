package com.example.project.controller;

import com.example.project.DTO.NewsDetailDTO;
import com.example.project.DTO.NewsSummaryDTO;
import com.example.project.model.SportsTranslate;
import com.example.project.repository.SportsTranslateRepository;
import com.example.project.service.HtmlProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final SportsTranslateRepository sportsTranslateRepository;
    private final HtmlProcessingService htmlProcessingService;

    public NewsController(SportsTranslateRepository sportsTranslateRepository, HtmlProcessingService htmlProcessingService) {
        this.sportsTranslateRepository = sportsTranslateRepository;
        this.htmlProcessingService = htmlProcessingService;
    }

    @GetMapping
    public ResponseEntity<List<NewsSummaryDTO>> getAllNews() {
        List<SportsTranslate> newsList = sportsTranslateRepository.findAll();

        List<NewsSummaryDTO> processedNewsList = newsList.stream()
                .map(news -> {
                    String title = htmlProcessingService.extractTitle(news.getContentTranslated());
                    return new NewsSummaryDTO(news.getId(), news.getUrl(), title);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(processedNewsList);
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<NewsDetailDTO> getNewsById(@PathVariable Integer newsId) {
        Optional<SportsTranslate> optionalNews = sportsTranslateRepository.findById(newsId);

        if (optionalNews.isPresent()) {
            SportsTranslate news = optionalNews.get();

            String title = htmlProcessingService.extractTitle(news.getContentTranslated());
            String content = htmlProcessingService.extractParagraphs(news.getContentTranslated());

            NewsDetailDTO newsDetailDTO = new NewsDetailDTO(title, content);
            return ResponseEntity.ok(newsDetailDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
