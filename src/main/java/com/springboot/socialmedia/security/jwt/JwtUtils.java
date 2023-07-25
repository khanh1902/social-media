package com.springboot.socialmedia.security.jwt;


import com.springboot.socialmedia.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    private final String jwtSecret = "================================khanhSecret================================";

    private final String jwtCookie = "auth-token";

    // Thời gian có hiệu lực của chuỗi jwt
    private final long jwtExpiration = 86400000L * 2L;  //hạn chuỗi jwt 1 ngay

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date(); // thời gian tạo
        Date expiryDate = new Date(now.getTime() + jwtExpiration); // thời gian hết hạn
        // Tạo chuỗi json web token từ userName của user.
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // cấp phát jwt cho user
                .setIssuedAt(now) // thời gian cấp
                .setExpiration(expiryDate) // thời gian hết hạn
                .signWith(key(), SignatureAlgorithm.HS256) //ký tên
                .compact(); // thực thi
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getEmailFromJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public ResponseCookie generateJWTCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromEmail(userPrincipal.getUsername());
        return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
    }

    public String generateTokenFromEmail(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key(), SignatureAlgorithm.HS256) //ký tên
                .compact();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null).path("/api").build();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
