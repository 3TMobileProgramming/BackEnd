// ChatResponseDto.java
package com.syu.noticeapi.dto;

import java.util.List;

public class ChatResponseDto {

    private String question;
    private String answer;
    private List<NoticeSummaryDto> notices;

    public ChatResponseDto() {}

    public ChatResponseDto(String question, String answer, List<NoticeSummaryDto> notices) {
        this.question = question;
        this.answer = answer;
        this.notices = notices;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<NoticeSummaryDto> getNotices() {
        return notices;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setNotices(List<NoticeSummaryDto> notices) {
        this.notices = notices;
    }
}