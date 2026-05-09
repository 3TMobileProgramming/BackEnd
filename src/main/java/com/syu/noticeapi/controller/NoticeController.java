package com.syu.noticeapi.controller;

import com.syu.noticeapi.dto.NoticeDetailDto;
import com.syu.noticeapi.dto.NoticeSummaryDto;
import com.syu.noticeapi.entity.Notice;
import com.syu.noticeapi.repository.NoticeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
@CrossOrigin(origins = "*")
public class NoticeController {

    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @GetMapping
    public List<NoticeSummaryDto> getNotices() {
        return noticeRepository.findAllByOrderByIdDesc().stream()
                .map(n -> new NoticeSummaryDto(n.getId(), n.getTitle(), n.getDate(), n.getUrl()))
                .toList();
    }

    @GetMapping("/{id}")
    public NoticeDetailDto getNoticeById(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 공지를 찾을 수 없습니다. id=" + id));
        return new NoticeDetailDto(notice.getId(), notice.getTitle(), notice.getDate(),
                notice.getUrl(), notice.getBody(), notice.getFetchedAt());
    }

    /**
     * 검색 엔드포인트 추가
     * GET /notices/search?keyword=장학
     * GET /notices/search?keyword=장학&category=장학
     */

    @GetMapping("/search")
    public List<NoticeSummaryDto> searchNotices(
            @RequestParam String keyword,
            @RequestParam(required = false) String category) {

        List<Notice> results = (category != null && !category.isBlank())
                ? noticeRepository.searchByFullTextAndCategory(keyword, category)
                : noticeRepository.searchByFullText(keyword);

        return results.stream()
                .map(n -> new NoticeSummaryDto(n.getId(), n.getTitle(), n.getDate(), n.getUrl()))
                .toList();
    }
    @GetMapping("/category/{category}")
    public List<NoticeSummaryDto> getByCategory(@PathVariable String category) {
        return noticeRepository.findByCategoryOrderByDateDesc(category).stream()
                .map(n -> new NoticeSummaryDto(n.getId(), n.getTitle(), n.getDate(), n.getUrl()))
                .toList();
    }
}