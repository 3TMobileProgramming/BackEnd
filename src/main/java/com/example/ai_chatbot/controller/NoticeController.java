package com.example.ai_chatbot.controller;

import com.example.ai_chatbot.entity.Notice;
import com.example.ai_chatbot.repository.NoticeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import java.util.List;



@RestController
public class NoticeController {

    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @GetMapping("/notice-test")
    public String noticeTest() {

        String url="https://example.com";

        if (noticeRepository.existsByUrl(url)){
            return "이미 저장된 공지입니다.";
        }

        Notice notice = new Notice();

        notice.setTitle("수강신청 안내");
        notice.setContent("2026학년도 1학기 수강신청 일정입니다.");
        notice.setCategory("학사");
        notice.setUrl(url);
        notice.setContentHash("test123");
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());

        noticeRepository.save(notice);

        return "공지 저장 완료!";
    }
    @GetMapping("/notices")
    public List<Notice> getNotices() {
        return noticeRepository.findAll();
    }

    @GetMapping("/search")
    public List<Notice> searchNotices(@RequestParam String keyword){
        return noticeRepository.findByTitleContainingOrContentContainingOrCategoryContaining(
                keyword,
                keyword,
                keyword
        );
    }
    @GetMapping("/notices/category")
    public List<Notice> getNoticeByCategory(@RequestParam String category){
        return noticeRepository.findByCategory(category);
    }
}