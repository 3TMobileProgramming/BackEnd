package com.syu.noticeapi.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class SyuNoticeCrawler {

    public Set<String> fetchPostUrls(String listUrlTemplate, int maxPages, int targetCount)
            throws IOException, InterruptedException {

        LinkedHashSet<String> postUrls = new LinkedHashSet<>();

        for (int page = 1; page <= maxPages && postUrls.size() < targetCount; page++) {
            String listUrl = String.format(listUrlTemplate, page);
            System.out.println("[LIST] " + listUrl);

            Set<String> urls = fetchPostUrlsFromList(listUrl);

            for (String url : urls) {
                if (postUrls.size() >= targetCount) {
                    break;
                }
                postUrls.add(url);
            }

            System.out.println("수집된 URL 수: " + postUrls.size());
            Thread.sleep(500);
        }

        return postUrls;
    }

    private Set<String> fetchPostUrlsFromList(String listUrl) throws IOException {
        Document doc = Jsoup.connect(listUrl)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        LinkedHashSet<String> result = new LinkedHashSet<>();

        for (Element a : doc.select("a[href]")) {
            String href = a.absUrl("href");

            if (href.contains("/etc/login")) {
                continue;
            }
            if (href == null || href.isBlank()) {
                continue;
            }
            if (!href.startsWith("https://www.syu.ac.kr/")) {
                continue;
            }
            if (!href.contains("/blog/")) {
                continue;
            }

            href = href.replaceAll("\\?.*$", "");
            href = href.replaceAll("#.*$", "");

            if (href.endsWith("/event") || href.endsWith("/event/")) {
                continue;
            }
            if (href.contains("/category/") || href.contains("/page/") || href.contains("/tag/")) {
                continue;
            }

            result.add(href);
        }

        return result;
    }
}