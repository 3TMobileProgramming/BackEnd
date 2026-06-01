package com.syu.noticeapi.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.syu.noticeapi.dto.ChatResponseDto;
import com.syu.noticeapi.dto.NoticeSummaryDto;
import com.syu.noticeapi.entity.Notice;
import com.syu.noticeapi.repository.NoticeRepository;

@Service
public class ChatService {

    private static final int GPT_NOTICE_LIMIT = 3;

    private final NoticeRepository noticeRepository;
    private final KeywordService keywordService;
    private final GptService gptService;

    public ChatService(
            NoticeRepository noticeRepository,
            KeywordService keywordService,
            GptService gptService
    ) {
        this.noticeRepository = noticeRepository;
        this.keywordService = keywordService;
        this.gptService = gptService;
    }

    public ChatResponseDto ask(String question) {

        String normalizedKeyword = keywordService.normalizeKeyword(question);
        String estimatedCategory = keywordService.estimateCategory(normalizedKeyword);

        System.out.println("입력 질문: " + question);
        System.out.println("정규화 키워드: " + normalizedKeyword);
        System.out.println("추정 카테고리: " + estimatedCategory);

        List<Notice> candidates = searchNotices(normalizedKeyword, estimatedCategory);

        if (candidates.isEmpty()) {
            return new ChatResponseDto(
                    question,
                    "관련 공지를 찾을 수 없습니다.",
                    List.of()
            );
        }

        List<Notice> selectedNotices = selectNoticesForGpt(
                candidates,
                normalizedKeyword,
                estimatedCategory
        );

        List<NoticeSummaryDto> result = selectedNotices.stream()
                .map(n -> new NoticeSummaryDto(
                        n.getId(),
                        n.getTitle(),
                        n.getDate(),
                        URLDecoder.decode(n.getUrl(), StandardCharsets.UTF_8)
                ))
                .collect(Collectors.toList());

        String prompt = gptService.createAnswerPrompt(question, selectedNotices);
        String answer = gptService.callGpt(prompt);

        return new ChatResponseDto(question, answer, result);
    }

    private List<Notice> searchNotices(String normalizedKeyword, String estimatedCategory) {

        List<Notice> searched;

        if (estimatedCategory != null && !"기타".equals(estimatedCategory)) {
            searched = noticeRepository.searchByFullTextAndCategory(
                    normalizedKeyword,
                    estimatedCategory
            );
        } else {
            searched = noticeRepository.searchByFullText(normalizedKeyword);
        }

        // FULLTEXT 검색 결과가 없을 때 기존 방식으로 fallback
        if (searched == null || searched.isEmpty()) {
            if (estimatedCategory != null && !"기타".equals(estimatedCategory)) {
                searched = noticeRepository.findByCategoryOrderByDateDesc(estimatedCategory);
            } else {
                searched = noticeRepository.findAllByOrderByDateDesc();
            }
        }

        return searched;
    }

    private List<Notice> selectNoticesForGpt(
            List<Notice> notices,
            String normalizedKeyword,
            String estimatedCategory
    ) {
        return notices.stream()
                .sorted(
                        Comparator.comparingInt(
                                (Notice notice) -> calculateNoticeScore(
                                        notice,
                                        normalizedKeyword,
                                        estimatedCategory
                                )
                        ).reversed()
                )
                .limit(GPT_NOTICE_LIMIT)
                .collect(Collectors.toList());
    }

    private int calculateNoticeScore(
            Notice notice,
            String normalizedKeyword,
            String estimatedCategory
    ) {
        int score = 0;

        if (notice.getTitle() != null
                && normalizedKeyword != null
                && notice.getTitle().contains(normalizedKeyword)) {
            score += 5;
        }

        if (notice.getCategory() != null
                && estimatedCategory != null
                && notice.getCategory().equals(estimatedCategory)) {
            score += 3;
        }

        if (notice.getBody() != null
                && normalizedKeyword != null
                && notice.getBody().contains(normalizedKeyword)) {
            score += 2;
        }

        return score;
    }
}