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
    public String saveTestNotices() {

        saveNoticeIfNotExists(
                "수강신청 안내",
                "2026학년도 1학기 수강신청 일정입니다.",
                "학사",
                "https://example.com/notice/1"
        );

        saveNoticeIfNotExists(
                "수강신청 변경 안내",
                "수강신청 기간이 변경되었습니다. 변경된 수강신청 일정을 확인하세요.",
                "학사",
                "https://example.com/notice/2"
        );

        saveNoticeIfNotExists(
                "2026학년도 학사일정 안내",
                "2026학년도 1학기 개강, 종강, 시험 기간에 대한 학사일정 안내입니다.",
                "학사",
                "https://example.com/notice/3"
        );

        saveNoticeIfNotExists(
                "장학금 신청 안내",
                "2026학년도 국가장학금 및 교내장학금 신청 기간을 안내합니다.",
                "장학",
                "https://example.com/notice/4"
        );

        saveNoticeIfNotExists(
                "취업 프로그램 신청 안내",
                "재학생 대상 취업 특강 및 현장실습 프로그램 신청 안내입니다.",
                "취업",
                "https://example.com/notice/5"
        );

        saveNoticeIfNotExists(
                "일반 공지사항 안내",
                "학교 홈페이지 이용 및 일반 공지사항 확인 방법을 안내합니다.",
                "기타",
                "https://example.com/notice/6"
        );

        return "테스트 공지 여러 개 저장 완료";
    }

    private void saveNoticeIfNotExists(String title, String content, String category, String url) {

        if (noticeRepository.existsByUrl(url)) {
            return ;
        }

        Notice notice = new Notice();

        notice.setTitle(title);
        notice.setContent(content);
        notice.setCategory(category);
        notice.setUrl(url);
        notice.setContentHash("test-" + url);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());

        noticeRepository.save(notice);
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
