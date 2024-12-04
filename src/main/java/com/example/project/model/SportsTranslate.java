package com.example.project.model;


import jakarta.persistence.*;

@Entity
@Table(name = "SPORTS_TRANSLATE")
public class SportsTranslate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "URL", nullable = false, length = 4000)
    private String url;

    @Column(name = "CONTENT_TRANSLATED", columnDefinition = "CLOB")
    private String contentTranslated;

    public SportsTranslate() {
    }

    public SportsTranslate(String url, String contentTranslated) {
        this.url = url;
        this.contentTranslated = contentTranslated;
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

    public String getContentTranslated() {
        return contentTranslated;
    }

    public void setContentTranslated(String contentTranslated) {
        this.contentTranslated = contentTranslated;
    }
}
