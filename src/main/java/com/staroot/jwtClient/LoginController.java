package com.staroot.jwtClient;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @GetMapping("/")
    public String loginRedirect() {
        return "login";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    /*
    @GetMapping("/main")
    public String mainForm() {
        return "main";
    }*/

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response, Model model) {
// 외부 REST API의 엔드포인트 URL
        RestClient restClient = RestClient.create();
        ResponseEntity responseEntity = restClient.post()
                .uri("http://localhost:8081/api/login?username="+username+"&password="+password)
                .contentType(TEXT_HTML)
                //.contentType(APPLICATION_JSON)
                //.body("")
                .retrieve()
                .toEntity(String.class);
                //.toBodilessEntity();
        logger.info("response toString : "+responseEntity.toString());
        logger.info("response getBody : "+responseEntity.getBody());
        String jwtToken = (String) responseEntity.getBody();

        // 쿠키에 JWT 토큰 저장
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setPath("/");
        response.addCookie(cookie);
        // 성공 시 홈 페이지로 이동
        return "main";
    }
}

