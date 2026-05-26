package com.example.ai_chatbot.service;

import com.example.ai_chatbot.dto.NoticeResponseDto;
import com.example.ai_chatbot.dto.SearchResponseDto;
import org.springframework.stereotype.Service;
import com.example.ai_chatbot.dto.AiAnswerResponseDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiSearchService {

    private static final int GPT_NOTICE_LIMIT=3;
    private final NoticeService noticeService;
    private final GptService gptService;

    public AiSearchService(NoticeService noticeService, GptService gptService) {
        this.noticeService = noticeService;
        this.gptService = gptService;
    }

    public String createPromptForQuestion(String question) {

        SearchResponseDto searchResult = noticeService.searchNotices(question);

        if (searchResult.getCount() == 0) {
            return "관련 공지를 찾을 수 없어 GPT 답변을 생성하지 않습니다.";
        }

        List<NoticeResponseDto> selectedNotices=selectNoticesForGpt(
                searchResult.getResults(),
                searchResult.getKeyword(),
                searchResult.getEstimatedCategory()
        );
        return gptService.createAnswerPrompt(
                question,
                searchResult.getResults()
        );
    }

    public AiAnswerResponseDto answerQuestion(String question) {

        SearchResponseDto searchResult = noticeService.searchNotices(question);
        if (searchResult.getCount() == 0) {
            return new AiAnswerResponseDto(
                    question,
                    searchResult.getKeyword(),
                    searchResult.getEstimatedCategory(),
                    "관련 공지를 찾을 수 없습니다.",
                    searchResult.getResults()
            );
        }

        List<NoticeResponseDto> selectedNotices=selectNoticesForGpt(
                searchResult.getResults(),
                searchResult.getKeyword(),
                searchResult.getEstimatedCategory()
        );

        String prompt=gptService.createAnswerPrompt(
                question,
                selectedNotices
        );

        String answer = gptService.callGpt(prompt);

        return new AiAnswerResponseDto(
                question,
                searchResult.getKeyword(),
                searchResult.getEstimatedCategory(),
                answer,
                selectedNotices
        );
    }
    //
    private List<NoticeResponseDto> selectNoticesForGpt(
            List<NoticeResponseDto> notices,
            String normalizedKeyword,
            String estimatedCategory
    ){
        return notices.stream()
                .sorted(
                        Comparator.comparingInt(
                                (NoticeResponseDto notice) -> calculateNoticeScore(
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
            NoticeResponseDto notice,
            String normalizedKeyword,
            String estimatedCategory
    ){
        int score=0;

        String title=notice.getTitle();
        String content=notice.getContent();
        String category= notice.getCategory();

        if (title!=null && normalizedKeyword != null && title.contains(normalizedKeyword)){
            score+=5;
        }
        if(category != null && estimatedCategory !=null && category.equals(estimatedCategory)){
            score +=3;
        }
        if (content != null && normalizedKeyword != null && content.contains(normalizedKeyword)){
            score+=2;
        }
        return score;
    }

}