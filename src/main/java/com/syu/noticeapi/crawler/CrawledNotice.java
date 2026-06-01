package com.syu.noticeapi.crawler;

import java.util.ArrayList;
import java.util.List;

public class CrawledNotice {

    private String url = "";
    private String title = "";
    private String date = "";
    private String rawBody = "";
    private String body = "";
    private String category = "";
    private List<String> attachments = new ArrayList<>();
    private List<TableData> tables = new ArrayList<>();
    private List<ClosedCourse> closedCourses = new ArrayList<>();
    private String fetchedAt = "";

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRawBody() {
        return rawBody;
    }

    public void setRawBody(String rawBody) {
        this.rawBody = rawBody;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public List<TableData> getTables() {
        return tables;
    }

    public void setTables(List<TableData> tables) {
        this.tables = tables;
    }

    public List<ClosedCourse> getClosedCourses() {
        return closedCourses;
    }

    public void setClosedCourses(List<ClosedCourse> closedCourses) {
        this.closedCourses = closedCourses;
    }

    public String getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(String fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}