package com.example.ai_chatbot.repository;

import com.example.ai_chatbot.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}