package com.syu.noticeapi.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KeywordService {

    private final Map<String, List<String>> keywordMap = Map.of(
            "수강신청", List.of("수강신청", "수강 신청", "강의신청", "강의 신청", "수업신청", "수업 신청", "신청기간"),
            "개강", List.of("개강", "학기 시작", "수업 시작", "개강일"),
            "종강", List.of("종강", "학기 종료", "수업 종료", "종강일"),
            "장학", List.of("장학", "장학금", "국가장학금", "장학 신청"),
            "휴학", List.of("휴학", "휴학신청", "휴학 신청"),
            "등록금", List.of("등록금", "납부", "등록 기간", "등록기간"),
            "취업", List.of("취업", "채용", "인턴", "현장실습")
    );

    private final Map<String, String> keywordCategoryMap = Map.of(
            "수강신청", "학사",
            "개강", "학사",
            "종강", "학사",
            "휴학", "학사",
            "등록금", "등록",
            "장학", "장학",
            "취업", "취업"
    );

    public String normalizeKeyword(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String normalizedInput = input.replaceAll("\\s+", "");

        for (String representativeKeyword : keywordMap.keySet()) {
            for (String synonym : keywordMap.get(representativeKeyword)) {
                String normalizedSynonym = synonym.replaceAll("\\s+", "");

                if (normalizedInput.contains(normalizedSynonym)) {
                    return representativeKeyword;
                }
            }
        }

        return input;
    }

    public String estimateCategory(String normalizedKeyword) {
        if (normalizedKeyword == null || normalizedKeyword.isBlank()) {
            return "기타";
        }

        return keywordCategoryMap.getOrDefault(normalizedKeyword, "기타");
    }
}