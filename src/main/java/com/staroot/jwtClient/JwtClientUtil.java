package com.staroot.jwtClient;
import io.jsonwebtoken.*;

import java.security.PublicKey;

public class JwtClientUtil {

    private static final String PUBLIC_KEY_FILE = "public_key.pem";

    private static final PublicKey PUBLIC_KEY = JwtClientKeyUtils.readPublicKeyFromFile(PUBLIC_KEY_FILE);


    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(PUBLIC_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(PUBLIC_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String token="1234";//jwt토큰발급서버로부터 발급받은 token

        // 토큰 검증
        boolean isValid = validateToken(token);
        System.out.println("Is Valid Token? " + isValid);

        // 토큰에서 사용자명 추출
        String username = getUsernameFromToken(token);
        System.out.println("Username from Token: " + username);
    }
}
