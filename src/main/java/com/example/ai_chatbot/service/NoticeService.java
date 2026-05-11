package com.example.ai_chatbot.service;

import com.example.ai_chatbot.entity.Notice;
import com.example.ai_chatbot.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository){
        this.noticeRepository=noticeRepository;
    }
    public String saveTestNotice(){
        String url= "https://example.com";

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
        return "공지 저장 완료";
    }

    public List<Notice> getAllNotices(){
        return noticeRepository.findAll();
    }

    public List<Notice> searchNotices(String keyword){ //키워드 검색 로직
        return noticeRepository.findByTitleContainingOrContentContainingOrCategoryContaining(
                keyword,
                keyword,
                keyword
        );
    }

    public List<Notice> getNoticesByCategory(String category){ //카테고리 기준 조회 로직
        return noticeRepository.findByCategory(category);
    }

}
