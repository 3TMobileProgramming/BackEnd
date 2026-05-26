package com.example.ai_chatbot.dto;


import lombok.Getter;

import java.util.List;

@Getter
public class AiAnswerResponseDto {

    private String question;
    private String normalizedKeyword;
    private String estimatedCategory;
    private String answer;
    private int count;
    private List<NoticeResponseDto> notices;

    public AiAnswerResponseDto(String question, String normalizedKeyword,String estimatedCategory, String answer, List<NoticeResponseDto> notices){
        this.question=question;
        this.normalizedKeyword=normalizedKeyword;
        this.estimatedCategory=estimatedCategory;
        this.answer=answer;
        this.notices=notices;
        this.count=notices.size();

    }

}
