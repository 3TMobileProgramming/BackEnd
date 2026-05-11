package com.example.ai_chatbot.repository;

import com.example.ai_chatbot.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByTitleContainingOrContentContainingOrCategoryContaining(
            String titleKeyword,
            String contentKeyword,
            String categoryKeyword
    );

    List<Notice> findByCategory(String Category);

    boolean existsByUrl(String Url); //sql문에서 중복되는 url이 있다면 true로 반환
}