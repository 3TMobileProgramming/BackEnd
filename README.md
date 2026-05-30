# 🏫 삼육대학교 공지사항 AI 앱 - Backend

## 프로젝트 소개
삼육대학교 공지사항을 자동으로 수집하고, AI 기반 검색 및 질의응답 기능을 제공하는 백엔드 서버입니다.

---
## 팀원

| 이름 | 담당 |
|------|------|
| 김수현 | DB 설계, 크롤링, API 구현, 검색 고도화 |
| 박성현 | AI 연동, GPT API, NLP 처리 |


## 기술 스택

| 분류 | 기술 |
|------|------|
| Framework | Spring Boot 3.3.5 |
| Language | Java 21 |
| Database | MySQL 8.0 |
| ORM | JPA / Hibernate |
| 크롤링 | Jsoup |
| 검색 | MySQL FULLTEXT + N-gram |
| AI | OpenAI GPT-4o-mini API |
| 빌드 | Maven |

---

## 프로그램 구조
src/main/java/com/syu/noticeapi/
├── controller/
│   ├── NoticeController.java      # 공지 조회/검색 API
│   ├── ChatController.java        # AI 채팅 API
│   └── CrawlerController.java     # 크롤링 API
├── service/
│   ├── NoticeCrawlerService.java  # 자동 크롤링 스케줄러
│   ├── ChatService.java           # 채팅 서비스
│   ├── GptService.java            # GPT API 연동
│   ├── KeywordService.java        # 키워드 추출 서비스
│   └── QuestionType.java          # 질문 유형 분류
├── crawler/
│   ├── SyuNoticeCrawler.java      # URL 수집
│   ├── SyuNoticeParser.java       # 데이터 파싱
│   ├── CrawledNotice.java         # 크롤링 데이터 객체
│   ├── JdbcNoticeRepository.java  # JDBC 저장
│   ├── ClosedCourse.java          # 폐강 데이터
│   └── TableData.java             # 테이블 데이터
├── repository/
│   └── NoticeRepository.java      # JPA 조회
├── entity/
│   └── Notice.java                # DB 테이블 매핑
└── dto/
├── NoticeSummaryDto.java       # 목록 응답
├── NoticeDetailDto.java        # 상세 응답
├── ChatRequestDto.java         # 채팅 요청
└── ChatResponseDto.java        # 채팅 응답

---

## 주요 기능

- **자동 크롤링**: 매일 오전 8시 학사/장학/행사 공지 자동 수집
- **공지 API**: 전체 목록, 상세 조회, 카테고리별, 키워드 검색
- **AI 채팅**: GPT-4o-mini 기반 자연어 질의응답
- **FULLTEXT 검색**: N-gram 파서 적용으로 한국어 검색 정확도 개선

---

## API 명세

| 기능 | 방식 | 주소 |
|------|------|------|
| 공지 전체 목록 | GET | /notices |
| 공지 상세 조회 | GET | /notices/{id} |
| 카테고리별 조회 | GET | /notices/category/{category} |
| 키워드 검색 | GET | /notices/search?keyword={keyword} |
| AI 채팅 | GET | /chat?question={question} |
| AI 채팅 | POST | /chat |
