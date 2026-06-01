package com.syu.noticeapi.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JdbcNoticeRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/syu_notice?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PASSWORD = "1225";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void save(CrawledNotice notice) {
        if (notice == null) {
            return;
        }

        if (notice.getUrl() == null || notice.getUrl().isBlank()) {
            System.out.println("[DB 저장 건너뜀] URL 없음");
            return;
        }

        String sql = "INSERT INTO notices (title, date, body, url, fetched_at, category, attachments) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "title = VALUES(title), " +
                "date = VALUES(date), " +
                "body = VALUES(body), " +
                "fetched_at = VALUES(fetched_at), " +
                "category = VALUES(category), " +
                "attachments = VALUES(attachments)";

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, safe(notice.getTitle(), "No Title"));
            pstmt.setString(2, safe(notice.getDate(), "No Date"));
            pstmt.setString(3, safe(notice.getBody(), ""));
            pstmt.setString(4, notice.getUrl());

            LocalDateTime fetchedAt;
            try {
                String fetchedAtStr = notice.getFetchedAt();
                if (fetchedAtStr == null || fetchedAtStr.isBlank()) {
                    fetchedAt = LocalDateTime.now();
                } else {
                    fetchedAt = LocalDateTime.parse(fetchedAtStr.trim(), FORMATTER);
                }
            } catch (Exception e) {
                fetchedAt = LocalDateTime.now();
            }

            pstmt.setTimestamp(5, Timestamp.valueOf(fetchedAt));
            pstmt.setString(6, safe(notice.getCategory(), ""));

            List<String> attachments = notice.getAttachments();
            String attachmentsStr = (attachments == null || attachments.isEmpty())
                    ? ""
                    : String.join(",", attachments);
            pstmt.setString(7, attachmentsStr);

            pstmt.executeUpdate();
            System.out.println("[DB 저장 완료] " + safe(notice.getTitle(), "No Title"));
        } catch (Exception e) {
            System.out.println("[DB 저장 실패] " + safe(notice.getTitle(), "No Title"));
            e.printStackTrace();
        }
    }

    private String safe(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}