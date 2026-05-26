package com.example.ai_chatbot.service;

import com.example.ai_chatbot.dto.NoticeResponseDto;
import com.example.ai_chatbot.dto.SearchResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AiSearchService {

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

        return gptService.createAnswerPrompt(
                question,
                searchResult.getResults()
        );
    }

    public String answerQuestion(String question) {

        SearchResponseDto searchResult = noticeService.searchNotices(question);

        if (searchResult.getCount() == 0) {
            return "관련 공지를 찾을 수 없습니다.";
        }

        String prompt = gptService.createAnswerPrompt(
                question,
                searchResult.getResults()
        );

        return gptService.callGpt(prompt);
    }
}