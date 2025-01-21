package com.example.demo.Service.auth;

import com.example.demo.Service.redis.RedisService;
import com.example.demo.exception.BusinessLogicException;
import com.example.demo.exception.ExceptionCode;
import com.example.demo.model.EmailVerificationResult;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MailService {

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long AUTH_CODE_EXPIRATION_MILLIS;

    @Value("${token.expiration.access}")
    private long ACCESS_TOKEN_EXPIRATION_MILLIS;

    @Value("${token.expiration.refresh}")
    private long REFRESH_TOKEN_EXPIRATION_MILLIS;

    @Value("${cookie.secure:false}") // 개발 환경: HTTPS가 없으니까 false
    private boolean isSecure;

    private final JavaMailSender emailSender;
    private final RedisService redisService;
    private final JwtService jwtService;

    private final MemberRepository memberRepository;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    public MailService(@Value("${spring.mail.auth-code-expiration-millis}") long authCodeExpirationMillis,
                       JavaMailSender emailSender,
                       RedisService redisService,
                       JwtService jwtService,
                       MemberRepository memberRepository) {
        this.AUTH_CODE_EXPIRATION_MILLIS = authCodeExpirationMillis;
        this.emailSender = emailSender;
        this.redisService = redisService;
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
    }

    public void sendEmail(String toEmail,
                          String title,
                          String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    private String createCode() {
/*        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MailService.createCode() exception occur");
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }*/
        return "111111";
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkExistingMember(toEmail);
        String title = "Demo 이메일 인증 번호";
        String authCode = this.createCode();
        this.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.AUTH_CODE_EXPIRATION_MILLIS));
    }

    // 로그인에 사용
    private void checkExistingMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            log.debug("Member does not exist. Email: {}", email);
            throw new BusinessLogicException(ExceptionCode.RESOURCE_NOT_FOUND);
        }
    }

    public EmailVerificationResult verifiedCode(String email, String authCode) {
        this.checkExistingMember(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        //boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        boolean authResult = redisAuthCode.equals(authCode);

        return EmailVerificationResult.of(authResult);
    }

    public Map<String, Object> generateTokens(String email, HttpServletResponse response) {
        // Access Token 및 Refresh Token 생성
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);

        // Refresh Token Redis에 저장
        redisService.setValues("RT:" + email, refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRATION_MILLIS));

        // 쿠키 생성
        ResponseCookie accessTokenCookie = CookieUtil.createCookie(
                "accessToken", accessToken, ACCESS_TOKEN_EXPIRATION_MILLIS, true, isSecure);
        ResponseCookie refreshTokenCookie = CookieUtil.createCookie(
                "refreshToken", refreshToken, REFRESH_TOKEN_EXPIRATION_MILLIS, true, isSecure);

        // 쿠키를 응답에 추가
        CookieUtil.addCookieToResponse(response, accessTokenCookie);
        CookieUtil.addCookieToResponse(response, refreshTokenCookie);

        // 성공 응답 생성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        return responseMap;
    }

}