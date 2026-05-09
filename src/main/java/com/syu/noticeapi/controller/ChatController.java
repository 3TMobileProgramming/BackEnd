package com.syu.noticeapi.controller;

import org.springframework.web.bind.annotation.*;

import com.syu.noticeapi.dto.ChatRequestDto;
import com.syu.noticeapi.dto.ChatResponseDto;
import com.syu.noticeapi.service.ChatService;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ChatResponseDto chatGet(@RequestParam String question) {
        return chatService.ask(question);
    }

    /**
     * POST 방식 추가 — GPT 연동 후 프론트에서 이걸로 전환 예정
     * 요청: POST /chat  Body: { "question": "장학금 신청 기간이 언제인가요?" }
     */
    @PostMapping
    public ChatResponseDto chatPost(@RequestBody ChatRequestDto request) {
        return chatService.ask(request.getQuestion());
    }
}