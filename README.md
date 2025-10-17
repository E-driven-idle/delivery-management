# 🍔 Delivery-Management

[//]: # (TODO - 요구사항 확인 후 상세 수정 필요)

<div align="center">

<img src="https://github.com/user-attachments/assets/298eb70e-d59f-445a-b8f0-f3f665710cee" width="512" />

🍰**고객과 사장님 모두 이용할 수 있는 배달 관리 웹 플랫폼**🍰

[🌐 사이트 바로가기]() | [📖 API 문서]()
</div>

## 💡 프로젝트 소개

> **"손님은 간편하게 주문하고, 사장님은 효율적으로 주문을 관리한다"**

**Deliver-Management**는 배달 주문의 전 과정을 효율적으로 관리할 수 있는 웹 기반 주문 관리 시스템입니다.

배달 주문은 이제 우리의 일상에 필수적인 서비스가 되었지만, 여전히 사용자와 점주 모두가 겪는 불편함이 존재합니다.

이 프로젝트는
- 손님이 더 빠르고 간편하게 주문할 수 있고,
- 사장님이 매장 내 주문을 한눈에 관리할 수 있도록 직관적이고 실용적인 환경을 제공합니다.

### 🎯 핵심 목표

- 고객에게 간편한 주문 경험 제공
- 점주에게 효율적인 주문 관리 기능 제공
- 주문, 결제, 메뉴 관리 등의 프로세스 자동화 및 최적화

---

<br>

## 🛠️ 기술 스택

### Backend
![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=spring)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring)
![Spring AI](https://img.shields.io/badge/Spring%20AI-000000?style=for-the-badge&logo=spring&logoColor=6DB33F)
![ChatClient](https://img.shields.io/badge/ChatClient-6DB33F?style=for-the-badge&logoColor=white)


### Database & Storage
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.0-4169E1?style=for-the-badge&logo=postgresql
)

### DevOps & Infrastructure
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=github-actions)
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=amazon-aws)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

### Test
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman)

### External APIs
![Kakao Map](https://img.shields.io/badge/Kakao%20Map-FFCD00?style=for-the-badge&logo=kakao)
![OpenAI API](https://img.shields.io/badge/OpenAI%20API-GPT--4o--mini-10A37F?style=for-the-badge&logo=openai&logoColor=white)
![Toss Payments](https://img.shields.io/badge/Toss%20Payments-0064FF?style=for-the-badge)

### Development Tools
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea)

### Collaboration
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack)
![ERDCloud](https://img.shields.io/badge/ERD%20Cloud-4285F4?style=for-the-badge)
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma)

---

<br>

## ✨ 핵심 기능

### 🍙 주문
- **기능 이름:** 
- **기능 이름:** 
- **기능 이름:** 

### 🏠 가게
- **가게 관리:** 
- **가게 평점:** 
- **가게 주소 등록:** 

### 💬 AI
- **AI 추천 설명 생성:** 점주가 신메뉴를 등록할 때, 원하면 AI가 추천해주는 해당 메뉴에 대한 설명을 자동으로 기입되도록 할 수 있습니다. 
- **AI 호출 로그 관리:** 관리자는 요청/응답 로그에 대한 목록/단건 조회, 키워드 기반 검색, 삭제/복구 할 수 있습니다.

### 🛍️ 결제
- **기능 이름:** 
- **기능 이름:** 
- **기능 이름:** 

### 🛒 장바구니
- **기능 이름:** 
- **기능 이름:** 
- **기능 이름:** 

### ⭐ 리뷰
- **기능 이름:** 
- **기능 이름:** 
- **리뷰 평점:** 

### ⚙️ 기타 기능
- **유저 인증/인가:** 로그인 여부 판단 및 로그인한 유저의 권한 기반 리소스 접근을 제어할 수 있습니다.

---

<br>

## 🏗️ 시스템 아키텍처

<img src="https://github.com/user-attachments/assets/da2cb565-19b9-4dff-8e12-1be80505844f" width="800" />

### 🔧 인프라 구성 예시
| 서비스 | 사양 | 역할 |
|--------|------|------|
| **EC2** | t3.medium | 애플리케이션 서버 |
| **RDS** | t4g.micro (MySQL) | 관계형 데이터베이스 |
| **ElastiCache** | t2.micro (Redis OSS) | 캐싱 및 세션 관리 |
| **ECR** | Private Repository | 컨테이너 이미지 저장 |
| **S3** | Standard | 사용자 업로드 파일 관리 |
| **MongoDB Atlas** | - | 채팅 데이터 저장 |



### 🚀 CI/CD 파이프라인 예시

```mermaid
graph LR
    A[GitHub] --> B[GitHub Actions]
    B --> C[Docker Build]
    C --> D[ECR Push]
    D --> E[EC2 Deploy]
```

---

<br>

## 💫 주요 기술적 의사결정

<details>
<summary>🔶 SpringAI + OpenAI API 연동</summary>

**🔹 배경**
- 메뉴 설명 생성 시 ChatGPT의 추천 설명을 자동으로 기입하기 위해 **OpenAI API 연동**이 필요함을 인식하였고,  
  Spring AI를 통해 OpenAI 모델(`gpt-4o-mini`)과의 통신을 구현하였습니다. 

**🔹 비교**
- **RestClient vs WebClient 비교**

| 항목             | **RestClient**                                   | **WebClient**                                                |
| -------------- | ------------------------------------------------ | ------------------------------------------------------------ |
| **도입 버전**      | Spring Framework 6.1 / Boot 3.2 이상               | Spring 5 (WebFlux 포함)                                        |
| **패키지 위치**     | `org.springframework.web.client.RestClient`      | `org.springframework.web.reactive.function.client.WebClient` |
| **프로그래밍 모델**   | 동기 (Synchronous)                                 | 비동기 (Asynchronous, Reactive Streams 기반)                      |
| **기반 기술**      | `RestTemplate`의 개선판 (Blocking I/O)               | Reactor 기반 (Non-Blocking I/O)                                |
| **사용 목적**      | 간단한 REST API 호출 (ex: 서버 간 내부 통신, 외부 REST API 연동) | 고성능, 대규모 비동기/스트리밍 처리 (ex: 실시간 데이터, 대량 호출)                    |
| **스레드 모델**     | 요청당 스레드 하나 점유 (Blocking)                         | 이벤트 루프 기반 (Non-Blocking, 효율적 리소스 사용)                         |
| **사용 편의성**     | ✅ 간단하고 직관적 (RestTemplate 대체용)                    | ⚙️ 약간 복잡하지만 고성능/리액티브                                         |
| **권장 사용 시나리오** | REST API 클라이언트 호출이 많지 않은 일반 백엔드 서비스              | 비동기 처리, 스트리밍, 대규모 외부 API 병렬 호출 환경                            |

**🔹 결론**
- **RestClient**는 단순·명확한 **동기식 모델**로, `RestTemplate`의 대체이자 표준화된 REST 통신 도구로 적합합니다.
- **WebClient**는 비동기/리액티브 기반으로 고성능이지만, 복잡도가 높습니다.
- **SpringAI의 `ChatClient` 내부에서는 상황에 따라 자동으로 `RestClient` 또는 `WebClient`를 선택**하여 사용합니다.  
  본 프로젝트(`Deliver-Management`)의 경우 **동기식 호출로도 충분**하므로, 내부적으로 `RestClient`가 사용됩니다.
</details>

<details>
<summary>🔶 적용한 기술 이름</summary>

**🔹 배경**
- 
- 

**🔹 비교**
- 
- 
- 

**🔹 결론**
- 
-
-
</details>

<details>
<summary>🔶 적용한 기술 이름</summary>

**🔹 배경**
- 
-

**🔹 비교**
- 
-
-

**🔹 결론**
- 
-
-
</details>

<details>
<summary>🔶 적용한 기술 이름</summary>

**🔹 배경**
- 
-

**🔹 비교**
- 
-
-

**🔹 결론**
- 
-
-
</details>

---

<br>

## 📊 성능 개선 예시 (추후에 딱히 없다면 아예 삭제 예정)

<details>
<summary>🚀 스프링 배치를 통한 91% 성능 개선</summary>

**🔹 기능 소개**
- 매일 자정마다 유저에게 데일리 질문 알림을 발송하는 기능 구현
- `Notification` 엔티티를 사용해 유저와 알림 내용을 저장

**🔹 기술 결정 과정**
- 기존 `findAll()` + 반복 저장 방식은 유저 수 증가 시 병목 발생
- Spring Batch + `chunk` 기반 처리로 대체
- `TaskExecutor`로 멀티스레드 병렬 처리 적용

**🔹 성능 테스트 결과**
- 기존 방식: 10만명 기준 평균 **58초**
- Spring Batch + 비동기 처리: **7초**
- JdbcCursorItemReader로 리팩토링 후: **5초**
- **총 91.3% 성능 개선** 달성

**🔹 회고**
- 단순 반복 저장 방식의 병목 문제를 Spring Batch로 해결
- reader/processor/writer 구조와 JDBC vs JPA 차이 학습
- 향후 파티셔닝 도입 및 병렬 Step 처리 고려 예정

</details>

<details>
<summary>🚀 Redis 캐싱을 통한 인기 랭킹 성능 개선</summary>

**🔹 기능 소개**
- 인기 장소 랭킹 데이터를 Redis에 캐싱하여 빠르게 조회

**🔹 기술 결정 과정**
- 기존 방식: QueryDSL로 DB 직접 조회 (좋아요 수 기준 정렬)
- 리팩토링 방식: Redis ZSet을 사용해 랭킹 구현
    - 좋아요 시 score 증가, 취소 시 감소
    - 캐시된 `placeId`로 DB에서 상세 데이터 조회

**🔹 성능 테스트 결과 (JMeter + Grafana)**
- 평균 응답시간: **509ms 개선 (약 3.1%)**
- 최소 응답시간: **5ms (SQL 대비 15배 빠름)**
- 3000명 동시 접속 시 **0% 오류율**
- Redis가 응답 지연과 부하 분산에 효과적임 확인

**🔹 회고**
- Redis 도입으로 캐시 기반 구조의 이점 체감
- 과부하 상황에서 서버 자원 한계를 시각화하며 대응 전략 필요성 인지
- 추후 TTL 설정, 조회 필드 최적화, 인덱싱 등도 개선 포인트로 도출

</details>

---

<br>

## 🚨 주요 트러블슈팅 예시

<details>
<summary>⚠️ Docker 빌드 캐시 이슈</summary>

- **문제**: 코드 수정 후 배포했으나 변경사항이 반영되지 않음<br/>
- **원인**: Docker 레이어 캐시로 인해 소스코드 변경이 감지되지 않음<br/>
- **해결**: `--no-cache` 옵션 사용 및 빌드 단계 최적화

</details>

<details>
<summary>⚠️ JPA Lazy Loading으로 인한 401 에러</summary>

- **문제**: Security 설정에 문제없음에도 401 Unauthorized 발생<br/>
- **원인**: LazyInitializationException이 Security Filter에서 401로 변환됨<br/>
- **해결**: QueryDSL fetch join 적용 및 GlobalExceptionHandler 보강<br/>

</details>

<details>
<summary>⚠️ Rate Limiting 버킷 초기화 문제</summary>

- **문제**: Bucket4j Rate Limiting이 매 요청마다 초기화됨<br/>
- **원인**: BucketConfiguration이 매번 새로 생성됨<br/>
- **해결**: 필드 레벨에서 고정된 Configuration 사용

</details>

---

<br>

## 🛡️ Test Coverage
![Test Coverage 캡처 이미지 넣을 곳]()

---

<br>

## 🗂️ 프로젝트 구조

```
📦 delivery-management
├── 📂 src/main/java/com/driven/dm
│   ├── 📂 global                  # 공통 엔티티/설정/예외처리
│   │   ├── 📂 config              # 설정 클래스들
│   │   │   ├── 📂 ai
│   │   │   ├── 📂 schedule
│   │   │   ├── 📂 security
│   │   │   └── 📂 swagger
│   │   ├── 📂 entity              # 공용 엔티티
│   │   ├── 📂 exception           # 예외 처리
│   │   └── 📄 JpaAuditingConfig
│   ├── 📂 ai                      # AI 추천 설명 생성 및 로그 관리
│   │   ├── 📂 application
│   │   │   ├── 📂 exception
│   │   │   └── 📂 service
│   │   ├── 📂 domain
│   │   │   └── 📂 entity
│   │   ├── 📂 infrastructure
│   │   │   └── 📂 repository
│   │   └── 📂 presentation
│   │       ├── 📂 controller
│   │       └── 📂 dto
│   ├── 📂 cart                    # 장바구니
│   │   ├── 📂 application
│   │   │   ├── 📂 exception
│   │   │   └── 📂 service
│   │   ├── 📂 domain
│   │   │   └── 📂 entity
│   │   ├── 📂 infrastructure
│   │   │   └── 📂 repository
│   │   └── 📂 presentation
│   │       ├── 📂 controller
│   │       └── 📂 dto
│   ├── 📂 menu                    # 메뉴
│   │   ├── 📂 application
│   │   │   ├── 📂 exception
│   │   │   └── 📂 service
│   │   ├── 📂 domain
│   │   │   └── 📂 entity
│   │   ├── 📂 infrastructure
│   │   │   └── 📂 repository
│   │   └── 📂 presentation
│   │       ├── 📂 controller
│   │       └── 📂 dto
│   ├── 📂 order                   # 주문
│   │   ├── 📂 application
│   │   │   ├── 📂 exception
│   │   │   └── 📂 service
│   │   ├── 📂 domain
│   │   │   └── 📂 entity
│   │   ├── 📂 infrastructure
│   │   │   └── 📂 repository
│   │   └── 📂 presentation
│   │       ├── 📂 controller
│   │       └── 📂 dto
│   ├── 📂 payment                 # 결제
│   │   ├── 📂 application
│   │   │   ├── 📂 exception
│   │   │   └── 📂 service
│   │   ├── 📂 domain
│   │   │   └── 📂 entity
│   │   ├── 📂 infrastructure
│   │   │   └── 📂 repository
│   │   └── 📂 presentation
│   │       ├── 📂 controller
│   │       └── 📂 dto
│   ├── 📂 review                  #  리뷰 
│   │   ├── 📂 application
│   │   │   ├── 📂 exception       
│   │   │   └── 📂 service        
│   │   ├── 📂 domain
│   │   │   └── 📂 entity        
│   │   ├── 📂 infrastructure
│   │   │   └── 📂 repository     
│   │   └── 📂 presentation
│   │       ├── 📂 controller    
│   │       └── 📂 dto            
        ●
        ●        
├── 📂 src/main/resources
│   ├── 📄 application.yml
│   └── 📄 application-local.yml   # 애플리케이션 설정
├── 📂 src/test 
└── 📄 gitmessage.txt              # 공통 커밋 템플릿
```

---

<br>

## 📝 API 문서 예시

자세한 API 문서는 [Postman Documentation]()에서 확인하실 수 있습니다.

### 주요 API 엔드포인트

| 기능 | Method | Endpoint | 설명 |
|------|--------|----------|------|
| 로그인 | GET | `/oauth2/authorization/google` | Google OAuth2 로그인 |
| 반려동물 등록 | POST | `/pet` | 사용자의 반려동물 등록 |
| 앨범 조회 | GET | `/users/my/albums` | 사용자가 기록한 사진 전체 조회 |
| 데일리 질문 | GET | `/daily-questions/today` | 오늘의 질문 조회|
| 플레이스 검색 | GET | `/places` | 반려동물 동반 장소 조회 |
| 게시글 목록 | GET | `/owner-boards` | 커뮤니티 게시글 전체 조회|
| 채팅 시작 | GET | `/chat/{chatId}` | 채팅방 입장 |
| 알림 전체 조회 | GET | `/notifications` | 오늘의 질문 알림 조회|
| 일정 생성 | POST | `/users/events` | 일정 생성 |


<br>

## 👥 팀원 소개

| 역할 | 이름  | 담당 기능                                | GitHub                              |
|------|-----|--------------------------------------|-------------------------------------|
| **BE 개발자** | 류창희 | 팀장, Shop 도메인, Menu 도메인, KaKaoMap API 연동 | [🍀](https://github.com/changhui98) |
| **BE 개발자** | 오세준 | 테크리더, User 도메인, 인증/인가, CI/CD         | [🍀](github.com/sejunO)             |
| **BE 개발자** | 김하정 | Ai 도메인, OpenAI API 연동                | [🍀](https://github.com/mueiso)     |
| **BE 개발자** | 천세경 | Payment 도메인, Toss payments API 연동    | [🍀](https://github.com/GyeongSe99) |
| **BE 개발자** | 박준형 | Cart 도메인, Review 도메인                 | [🍀](https://github.com/wnsgud0310) |

<br>

## 🏆 향후 개선 계획 예시

### 🔦 기능 확장
- [ ] 캘린더에서 반려동물 D-day 알림 추가
- [ ] 반려 동물의 성장 일지 기록하기

### 🔐 보안 강화
- [ ] Parameter Store를 통한 환경변수 중앙 관리
- [ ] Private Subnet으로 EC2 이동 및 NAT Gateway 구성

### 📈 확장성 개선
- [ ] Auto Scaling Group 적용
- [ ] Application Load Balancer 도입

### 🚀 성능 최적화
- [ ] Spring Batch 파티셔닝 적용

---

<div align="center">



🍜 **Made by E-driven-idle Team** 🍜
</div>
