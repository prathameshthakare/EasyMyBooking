package com.learn.authserviceforproject.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "FYNDNA-SUCCESS";

    public String extractEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    
    public String getToken(String emailId, String userRole) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(emailId)
                .claim("role", userRole)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, "FYNDNA-SUCCESS") // replace with your secret key
                .compact();
    }
    
    public boolean validateToken(String token, String emailId, String role) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey("FYNDNA-SUCCESS") 
                    .parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            String tokenEmailId = claims.getSubject();
            String tokenRole = claims.get("role", String.class);
            System.out.println("$$$$$" + tokenEmailId + "***"+ tokenRole);
            return emailId.equalsIgnoreCase(tokenEmailId) && role.equalsIgnoreCase(tokenRole);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    


}
