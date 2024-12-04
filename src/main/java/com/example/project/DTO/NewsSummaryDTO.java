package com.example.project.DTO;

public class NewsSummaryDTO {

    private Integer id;
    private String url;
    private String title;

    public NewsSummaryDTO(Integer id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
