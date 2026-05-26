package com.example.ai_chatbot.service;

import com.example.ai_chatbot.dto.NoticeResponseDto;
import org.springframework.beans.factory.annotation.Value; //api
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient; //api

import java.util.List;
import java.util.Map;

@Service
public class GptService {

    private final WebClient webClient;

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    public GptService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }


    public String createAnswerPrompt(String question, List<NoticeResponseDto> notices) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("너는 대학교 공지사항 안내 챗봇이다.\n");
        prompt.append("반드시 제공된 공지사항 정보만 근거로 답변해야 한다.\n");
        prompt.append("공지에 없는 내용은 추측하지 말고, 관련 공지를 찾을 수 없다고 답변해야 한다.\n\n");

        prompt.append("[사용자 질문]\n");
        prompt.append(question).append("\n\n");

        prompt.append("[관련 공지사항]\n");

        for (int i = 0; i < notices.size(); i++) {
            NoticeResponseDto notice = notices.get(i);

            prompt.append("공지 ").append(i + 1).append("\n");
            prompt.append("제목: ").append(notice.getTitle()).append("\n");
            prompt.append("카테고리: ").append(notice.getCategory()).append("\n");
            prompt.append("내용: ").append(notice.getContent()).append("\n");
            prompt.append("URL: ").append(notice.getUrl()).append("\n\n");
        }

        prompt.append("[답변 조건]\n");
        prompt.append("- 학생이 이해하기 쉽게 짧고 명확하게 답변해라.\n");
        prompt.append("- 날짜, 기간, 신청 방법이 있으면 우선적으로 정리해라.\n");
        prompt.append("- 마지막에 참고한 공지 제목을 함께 표시해라.\n");

        return prompt.toString();

    }

    public String callGpt(String prompt) {

        System.out.println("apiUrl = " + apiUrl); //오류 콘솔 출력
        System.out.println("model = " + model);
        System.out.println("apiKey exists = " + (apiKey != null && !apiKey.isBlank()));

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", prompt
        );

        try { //오류 쉽게 캐치
            return webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            return "GPT API 호출 중 오류가 발생했습니다: " + e.getMessage(); //오류 쉽게 캐치
        }
    }


}