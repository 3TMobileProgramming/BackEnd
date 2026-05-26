package com.example.ai_chatbot.controller;

import com.example.ai_chatbot.service.AiSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.ai_chatbot.dto.AiAnswerResponseDto;

@RestController
public class AiController {

    private final AiSearchService aiSearchService;

    public AiController(AiSearchService aiSearchService) {
        this.aiSearchService = aiSearchService;
    }

   @GetMapping("/ai/prompt")
    public String createPrompt(@RequestParam String question) {
        return aiSearchService.createPromptForQuestion(question);
    }


    @GetMapping("/ai/answer")
    public AiAnswerResponseDto answer(@RequestParam String question) {
        return aiSearchService.answerQuestion(question);
    }
}
