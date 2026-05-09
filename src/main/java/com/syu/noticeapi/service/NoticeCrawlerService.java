package com.syu.noticeapi.service;

import com.syu.noticeapi.crawler.CrawledNotice;
import com.syu.noticeapi.crawler.JdbcNoticeRepository;
import com.syu.noticeapi.crawler.SyuNoticeCrawler;
import com.syu.noticeapi.crawler.SyuNoticeParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class NoticeCrawlerService {

    /**
     * 매일 오전 8시에 자동 크롤링 실행
     * cron = "초 분 시 일 월 요일"
     * 변경하고 싶으면:
     *   매시간 실행 → "0 0 * * * *"
     *   매일 오전 6시 → "0 0 6 * * *"
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void scheduledCrawl() {
        System.out.println("[스케줄러] 자동 크롤링 시작");
        try {
            runCrawler();
        } catch (Exception e) {
            System.out.println("[스케줄러] 크롤링 실패: " + e.getMessage());
        }
    }

    public void runCrawler() throws Exception {
        SyuNoticeCrawler crawler = new SyuNoticeCrawler();
        SyuNoticeParser parser = new SyuNoticeParser();
        JdbcNoticeRepository repository = new JdbcNoticeRepository();

        Map<String, String> categoryUrls = new LinkedHashMap<>();
        categoryUrls.put("학사", "https://www.syu.ac.kr/academic/academic-notice/?c=&cat=&k=&paged=%d&t=");
        categoryUrls.put("장학", "https://www.syu.ac.kr/academic/scholarship-information/scholarship-notice/?c=&cat=&k=&paged=%d&t=");
        categoryUrls.put("행사", "https://www.syu.ac.kr/blog/category/syu-board/notice-syu-board/event/page/%d/");

        for (Map.Entry<String, String> entry : categoryUrls.entrySet()) {
            String category = entry.getKey();
            String listUrlTemplate = entry.getValue();

            System.out.println("===== " + category + " 공지 수집 시작 =====");

            Set<String> urls = crawler.fetchPostUrls(listUrlTemplate, 10, 100);
            System.out.println(category + " 수집된 URL 수: " + urls.size());

            for (String url : urls) {
                try {
                    CrawledNotice notice = parser.parseNotice(url);
                    notice.setCategory(category);
                    repository.save(notice);
                    System.out.println("[저장 완료] " + notice.getTitle());
                } catch (Exception e) {
                    System.out.println("[파싱 실패] " + url + " / 이유: " + e.getMessage());
                }
            }
        }

        System.out.println("===== 전체 크롤링 완료 =====");
    }
}