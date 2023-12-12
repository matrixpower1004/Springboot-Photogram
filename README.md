# 인스타그램 클론 SNS 프로젝트

## ERD
![sns_erd](https://github.com/matrixpower1004/Springboot-Photogram/assets/104916288/cfe91df2-ec94-4e8e-95e7-7f3998e33dd9)
https://www.erdcloud.com/d/uC6uYFLHoHed4huum

## API 명세서
https://mindextender.notion.site/API-31f082627a2a45b9b3c2a92a542aa032?pvs=4

## 기술 스택
| Backend | Java 8, Spring boot 2.6.6, Maven, Spring Security, JPA, Oauth 2.0 Client, QLRM, MySQL |
| --- | --- |
| Frontend(SSR) | JSP, JSTL, jQuery, JavaScript, Bootstrap 4 |

## 프로젝트 주요 내용
- 인스타그램 기능 구현을 통한 Java, Spring 관련 기술 학습 목적
- 구독 Feed, 좋아요, 구독, 회원 가입 등 주요 기능을 구현
- OAuth 2.0 Facebook login 적용
- 기본 로그인과 OAuth 2.0 로그인 세션 통합 관리
- Custom Exception Handling
- Reflection을 통한 유효성 검사 및 AOP를 이용한 유효성 검사 자동화

## 프로젝트에서 아쉬운 점
- Entity가 API에 직접 노출되는 현재의 설계가 적절한가?
- 양방향 매핑이 꼭 필요한가?
- 테스트 코드의 커버리지는 적절한가?
- AWS에 배포 후 부하 테스트 진행 필요.
- AWS에 배포시 사진 저장소를 AWS S3로 바꾸고 이미지 리사이징 배치 필요.

