# 🏫 AI 학교 공지 검색 어플리케이션 - Backend

## 프로젝트 소개

삼육대학교 공지사항을 자동으로 수집하고, AI 기반 검색 및 질의응답 기능을 제공하는 백엔드 서버입니다.

본 프로젝트는 공지사항 크롤링, DB 저장, 공지 조회/검색 API를 기반으로 하며, MySQL FULLTEXT + N-gram 검색과 GPT API 기반 AI 채팅 기능을 결합하여 사용자가 필요한 공지사항을 더 쉽게 찾고 이해할 수 있도록 지원합니다.

---

## 팀원

| 이름 | 담당 |
|------|------|
| 김수현 | DB 설계, 크롤링, API 구현, 검색 고도화 |
| 박성현 | AI 연동, GPT API, NLP 처리, 질문 키워드 정규화, 카테고리 추정, 공지 기반 AI 응답 처리 |

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Framework | Spring Boot 3.3.5 |
| Language | Java 21 |
| Database | MySQL 8.0 |
| ORM | JPA / Hibernate |
| 크롤링 | Jsoup |
| 검색 | MySQL FULLTEXT + N-gram |
| AI/NLP | OpenAI GPT-4o-mini API, 키워드 정규화, 카테고리 추정, 공지 기반 질의응답 |
| 빌드 | Maven |

---

## 프로그램 구조

```text
src/main/java/com/syu/noticeapi/
├── controller/
│   ├── NoticeController.java      # 공지 조회/검색 API
│   ├── ChatController.java        # AI 채팅 API
│   └── CrawlerController.java     # 크롤링 API
│
├── service/
│   ├── NoticeCrawlerService.java  # 자동 크롤링 스케줄러
│   ├── ChatService.java           # 사용자 질문 처리 및 공지 기반 응답 서비스
│   ├── GptService.java            # GPT API 연동 및 답변 생성
│   ├── KeywordService.java        # 질문 키워드 정규화 및 카테고리 추정
│   └── QuestionType.java          # 질문 유형 분류
│
├── crawler/
│   ├── SyuNoticeCrawler.java      # URL 수집
│   ├── SyuNoticeParser.java       # 데이터 파싱
│   ├── CrawledNotice.java         # 크롤링 데이터 객체
│   ├── JdbcNoticeRepository.java  # JDBC 저장
│   ├── ClosedCourse.java          # 폐강 데이터
│   └── TableData.java             # 테이블 데이터
│
├── repository/
│   └── NoticeRepository.java      # JPA 조회 및 FULLTEXT 검색
│
├── entity/
│   └── Notice.java                # DB 테이블 매핑
│
└── dto/
    ├── NoticeSummaryDto.java      # 목록 응답
    ├── NoticeDetailDto.java       # 상세 응답
    ├── ChatRequestDto.java        # 채팅 요청
    └── ChatResponseDto.java       # 채팅 응답
```

---

## 주요 기능

- **자동 크롤링**: 매일 오전 8시 학사/장학/행사 공지 자동 수집
- **공지 API**: 전체 목록, 상세 조회, 카테고리별 조회, 키워드 검색
- **FULLTEXT 검색**: MySQL FULLTEXT + N-gram 파서 적용으로 한국어 검색 정확도 개선
- **AI 채팅**: GPT-4o-mini 기반 자연어 질의응답
- **NLP 처리**: 사용자 질문의 키워드를 정규화하고 카테고리를 추정하여 관련 공지 검색에 활용
- **공지 기반 응답 생성**: 검색된 공지사항을 기반으로 GPT API가 사용자 질문에 대한 답변 생성

---

## AI / NLP 기능 상세

본 프로젝트의 AI/NLP 기능은 기존 공지 검색 기능을 대체하는 것이 아니라, 사용자의 자연어 질문을 더 정확하게 처리하기 위해 기존 검색 기능을 보완하는 역할을 합니다.

사용자가 입력한 질문을 분석하여 핵심 키워드를 정규화하고, 질문 의도에 맞는 공지 카테고리를 추정한 뒤, 관련 공지사항을 검색합니다. 이후 검색된 공지사항 내용을 GPT API에 전달하여 사용자에게 이해하기 쉬운 자연어 답변을 제공합니다.

### 처리 흐름

```text
사용자 질문 입력
        ↓
ChatController에서 GET 또는 POST 요청 수신
        ↓
ChatService에서 사용자 질문 처리
        ↓
KeywordService에서 질문 키워드 정규화
        ↓
KeywordService에서 예상 카테고리 추정
        ↓
NoticeRepository에서 관련 공지 검색
        ↓
검색된 공지사항 중 관련 공지 선별
        ↓
GptService에서 GPT API 호출
        ↓
ChatResponseDto 형태로 답변 반환
```

### 질문 키워드 정규화

사용자가 서로 다른 표현으로 질문하더라도 같은 의미의 키워드로 처리할 수 있도록 질문을 정규화합니다.

예시:

| 사용자 입력 표현 | 정규화 결과 |
|------|------|
| 수강 신청, 강의 신청, 수업 신청 | 수강신청 |
| 장학금, 국가장학금, 장학 신청 | 장학 |
| 납부, 등록 기간, 등록금 | 등록금 |
| 채용, 인턴, 현장실습 | 취업 |

### 카테고리 추정

정규화된 키워드를 기준으로 사용자의 질문이 어떤 공지 카테고리와 관련 있는지 추정합니다.

예시:

| 정규화 키워드 | 추정 카테고리 |
|------|------|
| 수강신청 | 학사 |
| 개강 | 학사 |
| 휴학 | 학사 |
| 장학 | 장학 |
| 등록금 | 등록 |
| 취업 | 취업 |

### GPT 기반 답변 생성

검색된 공지사항을 바탕으로 GPT API를 호출하여 사용자의 질문에 대한 답변을 생성합니다.

- 사용자의 자연어 질문을 기반으로 관련 공지 검색
- 검색된 공지사항 내용을 GPT API에 전달
- 공지 내용을 바탕으로 이해하기 쉬운 답변 생성
- 답변과 함께 참고 가능한 공지사항 정보 제공

---

## 검색 및 AI 응답 구조

```text
공지사항 크롤링
        ↓
DB 저장
        ↓
사용자 질문 입력
        ↓
키워드 정규화 및 카테고리 추정
        ↓
FULLTEXT + N-gram 기반 공지 검색
        ↓
관련 공지 선별
        ↓
GPT API 기반 답변 생성
        ↓
프론트엔드에 답변 및 참고 공지 반환
```

---

## API 명세

| 기능 | 방식 | 주소 | 설명 |
|------|------|------|------|
| 공지 전체 목록 | GET | `/notices` | 저장된 공지사항 전체 목록 조회 |
| 공지 상세 조회 | GET | `/notices/{id}` | 특정 공지사항 상세 조회 |
| 카테고리별 조회 | GET | `/notices/category/{category}` | 카테고리별 공지사항 조회 |
| 키워드 검색 | GET | `/notices/search?keyword={keyword}` | 키워드 기반 공지사항 검색 |
| 키워드 + 카테고리 검색 | GET | `/notices/search?keyword={keyword}&category={category}` | 키워드와 카테고리를 함께 사용한 검색 |
| AI 채팅 | GET | `/chat?question={question}` | 자연어 질문 기반 AI 답변 반환 |
| AI 채팅 | POST | `/chat` | JSON 형식의 질문을 받아 AI 답변 반환 |

---

## AI 채팅 API 예시

### GET 요청

```http
GET /chat?question=장학금 신청 기간이 언제인가요?
```

### POST 요청

```http
POST /chat
Content-Type: application/json
```

```json
{
  "question": "장학금 신청 기간이 언제인가요?"
}
```

### 응답 예시

```json
{
  "question": "장학금 신청 기간이 언제인가요?",
  "answer": "관련 장학 공지사항을 기준으로 신청 기간을 안내합니다.",
  "notices": [
    {
      "id": 1,
      "title": "2026학년도 장학금 신청 안내",
      "date": "2026-05-20",
      "url": "https://www.syu.ac.kr/..."
    }
  ]
}
```

---

## 기대 효과

- 사용자는 정확한 공지 제목을 몰라도 자연어 질문으로 공지사항 검색 가능
- 키워드 검색과 AI 응답을 함께 사용하여 검색 편의성 향상
- 질문 키워드 정규화와 카테고리 추정을 통해 관련 공지 검색 정확도 개선
- GPT API를 활용하여 공지 내용을 사용자에게 이해하기 쉬운 형태로 제공
- 참고 공지사항을 함께 제공하여 답변의 신뢰성 확보
