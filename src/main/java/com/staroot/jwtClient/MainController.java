package com.staroot.jwtClient;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

import static org.springframework.http.MediaType.TEXT_HTML;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @GetMapping("/test1")
    public String test1(HttpServletRequest request,Model model) {
        String jwtToken = getJwtTokenFromCookie(request);
        RestClient restClient = RestClient.create();
        String result = restClient.get()
                .uri("http://localhost:8081/api/hello")
                .header("Authorization",jwtToken)
                .retrieve()
                .body(String.class);
        logger.info("result::"+result);
        model.addAttribute("result",result);
        return "main";
    }

    @GetMapping("/test2")
    public String test2(HttpServletRequest request,Model model) {
        String jwtToken = getJwtTokenFromCookie(request);
        RestClient restClient = RestClient.create();
        String result = restClient.get()
                .uri("http://localhost:8081/api/jwtByCookie")
                .retrieve()
                .body(String.class);
        logger.info("result::"+result);
        model.addAttribute("result",result);
        return "main";
    }
    @GetMapping("/test3")
    public String test3(HttpServletRequest request,Model model) {
        String token = getJwtTokenFromCookie(request);
        String result="";
        if (JwtClientUtil.validateToken(token)) {
            String username = JwtClientUtil.getUsernameFromToken(token);
            result = "(LocalCheck) Hello, " + username + "!";
        } else {
            result = "(LocalCheck) Invalid token!";
        }
        logger.info(result);
        model.addAttribute("result",result);
        return "main";
    }

    private String getJwtTokenFromCookie(HttpServletRequest request) {
        // 쿠키에서 jwtToken 값을 찾아 반환
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}

