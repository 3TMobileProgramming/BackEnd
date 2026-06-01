package com.syu.noticeapi.dto;

public class NoticeSummaryDto {

    private Long id;
    private String title;
    private String date;
    private String url;

    public NoticeSummaryDto() {
    }

    public NoticeSummaryDto(Long id, String title, String date, String url) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}