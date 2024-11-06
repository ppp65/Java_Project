package com.example.project;

public class NewsDto {
    private String title;
    private String link;
    private String imageUrl;
    private String snippet;
    private String author;
    private String date;

    public NewsDto(String title, String link, String imageUrl, String snippet, String author, String date) {
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
        this.snippet = snippet;
        this.author = author;
        this.date = date;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSnippet() { return snippet; }
    public void setSnippet(String snippet) { this.snippet = snippet; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
