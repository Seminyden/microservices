package com.gmail.seminyden.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

@Log4j2
public class TraceIdFilter extends HttpFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String TRACE_ID        = "traceId";

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String traceId = getTraceId(request);
            log.debug("Add trace id {} to incoming request", traceId);
            MDC.put(TRACE_ID, traceId);
            chain.doFilter(request, response);

        } finally {
            MDC.remove(TRACE_ID);
        }
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (StringUtils.isNotBlank(traceId)) {
            return traceId;
        }
        return UUID.randomUUID().toString();
    }
}
