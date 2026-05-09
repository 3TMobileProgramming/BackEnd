package com.syu.noticeapi.repository;

import com.syu.noticeapi.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByOrderByIdDesc();
    List<Notice> findAllByOrderByDateDesc();
    List<Notice> findByCategoryOrderByDateDesc(String category);

    // FULLTEXT + N-gram 검색으로 교체
    @Query(value = "SELECT * FROM notices WHERE MATCH(title, body) AGAINST(:keyword IN BOOLEAN MODE) ORDER BY date DESC LIMIT 20", nativeQuery = true)
    List<Notice> searchByFullText(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM notices WHERE category = :category AND MATCH(title, body) AGAINST(:keyword IN BOOLEAN MODE) ORDER BY date DESC LIMIT 20", nativeQuery = true)
    List<Notice> searchByFullTextAndCategory(@Param("keyword") String keyword, @Param("category") String category);
}