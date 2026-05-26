package com.example.ai_chatbot.service;

import com.example.ai_chatbot.entity.Notice;
import com.example.ai_chatbot.repository.NoticeRepository;
import org.springframework.stereotype.Service;


import com.example.ai_chatbot.dto.NoticeResponseDto;
import com.example.ai_chatbot.dto.SearchResponseDto;
import java.util.stream.Collector;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final KeywordService keywordService;

    public NoticeService(NoticeRepository noticeRepository, KeywordService keywordService){
        this.noticeRepository=noticeRepository;
        this.keywordService=keywordService;
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

    public List<NoticeResponseDto> getAllNotices() {
        return noticeRepository.findAll()
                .stream()
                .map(NoticeResponseDto::new)
                .collect(Collectors.toList());
    }

    public SearchResponseDto searchNotices(String keyword){ //키워드 검색 로직
       String normalizedKeyword=keywordService.normalizeKeyword(keyword);
       String estimatedCategory=keywordService.estimateCategory(normalizedKeyword);

       System.out.println("입력 키워드: "+keyword);
       System.out.println("정규화 키워드: "+normalizedKeyword);
       System.out.println("추정 카테고리: "+estimatedCategory);


       List<NoticeResponseDto> results=
               noticeRepository.findByTitleContainingOrContentContainingOrCategoryContaining(
                normalizedKeyword,
                normalizedKeyword,
                normalizedKeyword
               )
                .stream()
                .map(NoticeResponseDto::new)
                .collect(Collectors.toList());

       return new SearchResponseDto(normalizedKeyword, estimatedCategory, results);
    }

    public List<NoticeResponseDto> getNoticesByCategory(String category){ //카테고리 기준 조회 로직

        return noticeRepository.findByCategory(category)
                .stream()
                .map(NoticeResponseDto::new)
                .collect(Collectors.toList());
    }

}
