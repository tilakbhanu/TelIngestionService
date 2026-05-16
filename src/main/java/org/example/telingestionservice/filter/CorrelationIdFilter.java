package org.example.telingestionservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.telingestionservice.producer.KafkaMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CHILD_CORRELATION_ID_HEADER = "X-Child-Correlation-ID";
    private static final Logger jwtLogger = LoggerFactory.getLogger(KafkaMessageProducer.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String parentId = request.getHeader(CORRELATION_ID_HEADER);
        String childId = UUID.randomUUID().toString();

        // Logic: If no CorrelationId provided, current request is the root (Parent)
        if (parentId == null || parentId.isEmpty()) {
            parentId = childId; // No parent exists, so this request is the root.
        }
        jwtLogger.info("Cor id {}", parentId);
        // Add to MDC for logging (this will now appear in every log line automatically)
        MDC.put("parentId", parentId);
        MDC.put("childId", childId);

        // Add to Response so the client knows what IDs were used
        response.addHeader(CORRELATION_ID_HEADER, parentId);
        response.addHeader(CHILD_CORRELATION_ID_HEADER, childId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clean up MDC to prevent memory leaks/pollution in thread pools
            MDC.clear();
        }
    }
}