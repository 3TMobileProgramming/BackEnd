package com.syu.noticeapi.dto;

import java.time.LocalDateTime;

public class NoticeDetailDto {

    private Long id;
    private String title;
    private String date;
    private String url;
    private String body;
    private LocalDateTime fetchedAt;

    public NoticeDetailDto() {
    }

    public NoticeDetailDto(Long id, String title, String date, String url, String body, LocalDateTime fetchedAt) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.url = url;
        this.body = body;
        this.fetchedAt = fetchedAt;
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

    public String getBody() {
        return body;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
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

    public void setBody(String body) {
        this.body = body;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}