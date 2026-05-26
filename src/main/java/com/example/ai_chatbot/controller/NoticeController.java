package com.example.ai_chatbot.controller;

import com.example.ai_chatbot.dto.NoticeResponseDto;
import com.example.ai_chatbot.entity.Notice;
//import com.example.ai_chatbot.repository.NoticeRepository;

import com.example.ai_chatbot.dto.SearchResponseDto;
import com.example.ai_chatbot.service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import java.util.List;



@RestController
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice-test")
    public String noticeTest() {
            return noticeService.saveTestNotices();
        }

    @GetMapping("/notices")
    public List<NoticeResponseDto> getNotices() {
        return noticeService.getAllNotices();
    }

    @GetMapping("/search")
    public SearchResponseDto searchNotices(@RequestParam String keyword){
        return noticeService.searchNotices(keyword);
    }
    @GetMapping("/notices/category")
    public List<NoticeResponseDto> getNoticeByCategory(@RequestParam String category){
        return noticeService.getNoticesByCategory(category);
    }
}