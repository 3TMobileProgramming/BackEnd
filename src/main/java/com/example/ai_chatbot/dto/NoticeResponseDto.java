package com.example.ai_chatbot.dto;

import com.example.ai_chatbot.entity.Notice;
import lombok.Getter;

@Getter
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private String category;
    private String url;

    public NoticeResponseDto(Notice notice){
        this.id=notice.getId();
        this.title = notice.getTitle();
        this.content=notice.getContent();
        this.category=notice.getCategory();
        this.url=notice.getUrl();
    }


}
