package com.gmail.seminyden.interceptor;

import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Log4j2
public class TraceIdInterceptor implements ClientHttpRequestInterceptor {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String traceId = MDC.get("traceId");
        log.debug("Add traceId {} to sent request", traceId);
        if (traceId != null) {
            request.getHeaders().add(TRACE_ID_HEADER, traceId);
        }
        return execution.execute(request, body);
    }
}
