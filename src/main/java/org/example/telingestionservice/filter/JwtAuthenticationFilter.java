package org.example.telingestionservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JWT Authentication Filter");
        System.out.println("Token extraction will be here and Set User Context if required _but ingestion may not be required.");
        // 1. Extract your token from the header
//        String jwt = extractToken(request);
//
//        if (jwt != null && validateToken(jwt)) {
//            // 2. Create the Authentication object
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(username, null, authorities);
//
//            // 3. Set the SecurityContext
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }

        // 4. Continue the filter chain
        System.out.println("PATH = " + request.getServletPath());
        filterChain.doFilter(request, response);
    }
}