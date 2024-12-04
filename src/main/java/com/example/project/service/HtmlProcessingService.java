package com.example.project.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class HtmlProcessingService {

    public String extractTitle(String html) {
        Document document = Jsoup.parse(html);
        String title = document.select("section[data-title]").attr("data-title");
        return title.isEmpty() ? "제목 없음" : title;
    }

    public String extractParagraphs(String html) {
        Document document = Jsoup.parse(html);
        Elements paragraphs = document.select("p");
        StringBuilder content = new StringBuilder();

        paragraphs.forEach(paragraph -> content.append(paragraph.text()).append("\n"));

        return content.toString().trim();
    }
}
