package com.example.ai_chatbot.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchResponseDto {

    private String keyword;
    private String estimatedCategory;
    private int count;
    private String message;
    private List<NoticeResponseDto> results;

    public SearchResponseDto(String keyword, String estimatedCategory, List<NoticeResponseDto> results) {
        this.keyword = keyword;
        this.estimatedCategory = estimatedCategory;
        this.count = results.size();
        this.results = results;

        if (results.isEmpty()) {
            this.message = "관련 공지를 찾을 수 없습니다.";
        } else {
            this.message = "검색 결과가 있습니다.";
        }

    }
    public SearchResponseDto(String keyword,List<NoticeResponseDto> results){
        this(keyword,"기타",results);
    }
}
