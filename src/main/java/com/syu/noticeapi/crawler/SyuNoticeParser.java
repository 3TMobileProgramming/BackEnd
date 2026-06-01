package com.syu.noticeapi.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyuNoticeParser {

    private static final DateTimeFormatter FETCHED_AT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CrawledNotice parseNotice(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        Element content = firstNonNull(
                doc.selectFirst("article"),
                doc.selectFirst(".post"),
                doc.selectFirst("main"),
                doc.selectFirst(".entry-content"),
                doc.selectFirst(".post-content"),
                doc.body()
        );

        if (content == null) {
            throw new IllegalStateException("본문 영역을 찾을 수 없음");
        }

        content.select("nav, footer, aside, script, style, button").remove();
        content.select(".share, .sns, .social, .kakaotalk, .facebook, .twitter").remove();
        content.select(".entry-meta, .post-meta, .meta, .views, .view").remove();

        String raw = content.text();
        String bodyAll = cleanText(raw);

        String attachments = doc.select(".attachment").text();

        CrawledNotice notice = new CrawledNotice();
        notice.setUrl(url);
        notice.setTitle(extractTitle(doc, bodyAll));
        notice.setDate(extractDate(doc, bodyAll));
        notice.setRawBody(bodyAll);
        notice.setBody(trimBody(bodyAll));
        notice.setFetchedAt(LocalDateTime.now().format(FETCHED_AT_FORMAT));

        List<String> attachmentsList = new ArrayList<>();
        if (attachments != null && !attachments.isBlank()) {
            attachmentsList.add(attachments.trim());
        }
        notice.setAttachments(attachmentsList);

        notice.setCategory("");

        return notice;
    }

    private String extractTitle(Document doc, String bodyAll) {
        Element titleEl = firstNonNull(
                doc.selectFirst("h1"),
                doc.selectFirst("h2"),
                doc.selectFirst("h3"),
                doc.selectFirst(".entry-title"),
                doc.selectFirst(".post-title"),
                doc.selectFirst(".board-title"),
                doc.selectFirst(".title")
        );

        if (titleEl != null) {
            String title = cleanTitle(titleEl.text());
            if (!title.isBlank() && !title.equalsIgnoreCase("No Title")) {
                return title;
            }
        }

        String docTitle = doc.title();
        if (docTitle != null && !docTitle.isBlank()) {
            docTitle = docTitle.replace(" – 삼육대학교", "")
                               .replace("- 삼육대학교", "")
                               .trim();
            if (!docTitle.isBlank()) {
                return docTitle;
            }
        }

        if (bodyAll != null && !bodyAll.isBlank()) {
            String[] lines = bodyAll.split("\\s{2,}|\\n");
            for (String line : lines) {
                String t = cleanTitle(line);
                if (t.length() >= 5
                        && !t.contains("조회수")
                        && !t.contains("카카오톡")
                        && !t.equals("학사 공지")
                        && !t.equals("장학 공지")
                        && !t.equals("행사 공지")) {
                    return t;
                }
            }
        }

        return "No Title";
    }

    private String extractDate(Document doc, String bodyAll) {
        String allText = cleanText(doc.text()) + " " + bodyAll;

        Pattern pattern = Pattern.compile(
                "\\b\\d{4}\\.\\d{2}\\.\\d{2}\\b" +      // 2026.03.10
                "|\\b\\d{4}-\\d{2}-\\d{2}\\b" +         // 2026-03-10
                "|\\b\\d{2}/\\d{2}/\\d{4}\\b"           // 03/10/2026
        );

        Matcher matcher = pattern.matcher(allText);
        if (matcher.find()) {
            return matcher.group();
        }

        return "No Date";
    }

    private String cleanTitle(String title) {
        if (title == null) return "";
        return title.replaceAll("\\s+", " ").trim();
    }

    private String cleanText(String text) {
        if (text == null) return "";
        return text.replaceAll("\\s+", " ").trim();
    }

    private String trimBody(String body) {
        if (body == null) return "";
        int maxLength = 2000;
        return body.length() > maxLength ? body.substring(0, maxLength) : body;
    }

    @SafeVarargs
    private final <T> T firstNonNull(T... candidates) {
        for (T candidate : candidates) {
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }
}