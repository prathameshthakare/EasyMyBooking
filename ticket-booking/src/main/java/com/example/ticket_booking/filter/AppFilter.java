package com.example.ticket_booking.filter;

import com.example.ticket_booking.context.*;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//import com.learn.easeMyBooking.context.UserContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = Jwts.parser().setSigningKey("FYNDNA-SUCCESS").parseClaimsJws(token).getBody();
                String emailId = claims.getSubject();
                String role = claims.get("role", String.class);

                if (emailId != null && role != null) {
                    UserContext userContext = new UserContext(emailId, role);
                    request.setAttribute("userContext", userContext);
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Token");
                }
            } catch (Exception e) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Token");
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token is Missing");
        }
    }
}
