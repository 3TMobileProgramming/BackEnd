package com.syu.noticeapi.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.syu.noticeapi.dto.ChatResponseDto;
import com.syu.noticeapi.dto.NoticeSummaryDto;
import com.syu.noticeapi.entity.Notice;
import com.syu.noticeapi.repository.NoticeRepository;

@Service
public class ChatService {

    private final NoticeRepository noticeRepository;

    public ChatService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public ChatResponseDto ask(String question) {

        String category = extractCategory(question);
        List<String> keywords = extractKeywords(question);

        List<Notice> all = (category != null)
                ? noticeRepository.findByCategoryOrderByDateDesc(category)
                : noticeRepository.findAllByOrderByDateDesc();

        // 점수 기반 관련 공지 추출
        List<Notice> ranked = all.stream()
                .map(n -> new ScoredNotice(n, score(n, keywords)))
                .filter(sn -> sn.score > 0)
                .sorted((a, b) -> b.score - a.score)
                .limit(5)
                .map(sn -> sn.notice)
                .collect(Collectors.toList());

        if (ranked.isEmpty()) {
            ranked = all.stream().limit(5).collect(Collectors.toList());
        }

        List<NoticeSummaryDto> result = ranked.stream()
                .map(n -> new NoticeSummaryDto(
                        n.getId(),
                        n.getTitle(),
                        n.getDate(),
                        URLDecoder.decode(n.getUrl(), StandardCharsets.UTF_8)
                ))
                .collect(Collectors.toList());

        String answer = generateAnswer(question, keywords, result);

        return new ChatResponseDto(question, answer, result);
    }

    // =====================================================
    // 키워드 추출 — 하드코딩 대신 형태소 단위로 분리
    // GPT 연동 시 이 메서드 전체를 GPT 호출로 교체 예정
    // =====================================================
    private List<String> extractKeywords(String question) {
        List<String> keywords = new ArrayList<>();

        String[] tokens = question.split("\\s+|(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        for (String token : tokens) {
            String t = token.replaceAll("[^가-힣a-zA-Z0-9]", "").trim();
            if (t.length() >= 2) keywords.add(t);
        }

        // 불용어 제거
        List<String> stopwords = List.of("있나요", "언제", "어디서", "알려줘", "궁금해", "인가요", "알고싶어", "입니다", "있어요");
        keywords.removeIf(k -> stopwords.stream().anyMatch(s -> k.contains(s)));

        if (keywords.isEmpty()) keywords.add(question.trim());

        return keywords;
    }

    // =====================================================
    // 카테고리 추출
    // =====================================================
    private String extractCategory(String question) {
        if (question.contains("장학")) return "장학";
        if (question.contains("수강") || question.contains("폐강")
                || question.contains("등록") || question.contains("학사")) return "학사";
        if (question.contains("행사") || question.contains("이벤트")) return "행사";
        return null;
    }

    // =====================================================
    // 점수 계산
    // =====================================================
    private int score(Notice notice, List<String> keywords) {
        int score = 0;
        for (String k : keywords) {
            if (notice.getTitle() != null && notice.getTitle().contains(k)) score += 10;
            if (notice.getBody() != null && notice.getBody().contains(k)) score += 5;
        }
        return score;
    }

    // =====================================================
    // 자연어 답변 생성
    // GPT 연동 시 이 메서드를 GPT 호출로 교체 예정
    // =====================================================
    private String generateAnswer(String question, List<String> keywords, List<NoticeSummaryDto> result) {
        if (result.isEmpty()) {
            return "'" + String.join(", ", keywords) + "' 관련 공지를 찾지 못했습니다. 다른 키워드로 검색해보세요.";
        }

        String keyword = keywords.isEmpty() ? "" : keywords.get(0);

        if (question.contains("언제")) {
            return "'" + keyword + "' 관련 최신 공지 기준으로 안내드립니다. 아래 공지를 확인하세요.";
        }
        if (question.contains("신청")) {
            return "'" + keyword + "' 신청 관련 공지입니다. 공지 본문에서 신청 기간과 방법을 확인하세요.";
        }
        if (question.contains("기간") || question.contains("일정")) {
            return "'" + keyword + "' 관련 일정 공지입니다. 날짜를 꼭 확인하세요.";
        }

        return "'" + keyword + "' 관련 최신 공지 " + result.size() + "건입니다.";
    }

    private static class ScoredNotice {
        Notice notice;
        int score;
        ScoredNotice(Notice notice, int score) {
            this.notice = notice;
            this.score = score;
        }
    }
}