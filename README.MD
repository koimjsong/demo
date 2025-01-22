# Demo Project
## branch dev1
- 토큰 발급 시 httpOnly cookie 에 저장하여 javascript에서는 접근 가능하지 못하게 변경 -> 쿠키 생성 시 isHttpOnly를 true로 설정
</br></br> 
API 요청시 withCredentials: true 사용하도록 axios interceptor 수정.
</br></br>
- thymeleaf-extras-springsecurity6 사용 - 아직 안함. 
</br>
1) 토큰이 httpOnly Cookie에 있기 때문에 back 단에서 토큰 검증하도록 하기 위해서 spring security 설정함. 
</br></br>
2) http://localhost:8080/email-login 으로 들어가서 이메일 인증 받는 것 먼저 해야 axios interceptor 기능이 잘 동작하는 지 확인 가능함.
</br></br>
3) 테스트 위해 토큰 만료/유효 시간을 다음과 같이 설정. 변경은  application.yaml 에서 하면 됨. 
</br></br>
accessToken: 30초</br>
refreshToeken: 5분
</br></br>
4) 다음 코드는 타임리프 예제 만들어본 것 뿐이고 API 연동하는 부분은 없다.
* src/main/resources/static/js/pages/auth/login.js
* src/main/resources/static/js/pages/auth/verifyPhone.js

5) test 경로에 있는 파일은 라이브러리 테스트 위해서 예제로 만들어본 것.

## 예제 동작
이메일 인증 받아서 코드 맞으면 accessToken과 refreshToken 발급이 되고 쿠키에 추가되어 헤더에 담기게 된다. 
</br></br>* 테스트 용도로 만든거라 토큰 암호화는 생략함.</br>
</br>로그인 해서 뜬 창(/home)에서 동작을 하면 api 호출이 되는데, 이때 axios interceptor 로 토큰이 유효한지 검증하도록 한다.

## H2 DB에 user data 저장 
src/main/resources/data.sql 에 등록할 User 정보(email) 추가하고 서버 돌리면 됨.

## Axios 공통 api 호출함수
1) import/export Javascript의 모듈 시스템 활용(현재)
2) js파일을 script 태그로 HTML에 포함 -> 함수 이름이 전역으로 노출되어 중복되는 이름의 함수가 구현되어 있을 경우, 다른 스크립트와 충돌 할 수 있음.
3) IIFE (즉시 실행 함수 표현식) + 클로저


## Redis
이메일 인증시, 이메일 주소와 인증코드를 레디스에 저장. 검증 시 레디스에서 꺼내와서 검증함.
</br></br>
```C:\Program Files\Redis\redis-cli.exe```
### 서버시작
#### 1. Redis 설치 디렉토리로 이동:
```cd C:\path\to\redis```
#### 2. Redis 서버 실행:
```redis-server.exe redis.windows.conf```

### 서버중지
```ctrl + c```

### 서버확인
```redis-cli ping```

