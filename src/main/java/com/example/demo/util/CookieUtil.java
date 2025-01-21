package com.example.demo.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import java.time.Duration;

public class CookieUtil {

    /**
     * 쿠키 생성 메서드
     *
     * @param name 쿠키 이름
     * @param value 쿠키 값
     * @param maxAge 쿠키의 유효 기간 (초 단위)
     * @param isHttpOnly HttpOnly 플래그
     * @param isSecure Secure 플래그
     * @return 생성된 ResponseCookie 객체
     */
    public static ResponseCookie createCookie(String name, String value, long maxAge, boolean isHttpOnly, boolean isSecure) {
        return ResponseCookie.from(name, value)
                .httpOnly(isHttpOnly)
                .secure(isSecure)
                .path("/") // 애플리케이션 전체에 대해 쿠키 전송
                .maxAge(Duration.ofSeconds(maxAge))
                .sameSite("Strict") // CSRF 방어
                .build();
    }

    /**
     * 쿠키를 응답 헤더에 추가하는 메서드
     *
     * @param response HttpServletResponse 객체
     * @param cookie ResponseCookie 객체
     */
    public static void addCookieToResponse(HttpServletResponse response, ResponseCookie cookie) {
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
