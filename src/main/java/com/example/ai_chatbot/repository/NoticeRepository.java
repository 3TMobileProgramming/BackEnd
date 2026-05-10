package com.example.ai_chatbot.repository;

import com.example.ai_chatbot.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByTitleContainingOrContentContaining(
            String titleKeyword,
            String contentKeyword
    );
}