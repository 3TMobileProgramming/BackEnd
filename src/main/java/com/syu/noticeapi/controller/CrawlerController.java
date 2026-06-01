package com.syu.noticeapi.controller;

import com.syu.noticeapi.service.NoticeCrawlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {

    private final NoticeCrawlerService crawlerService;

    public CrawlerController(NoticeCrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/run")
    public String runCrawler() throws Exception {
        crawlerService.runCrawler();
        return "크롤링 완료";
    }
}