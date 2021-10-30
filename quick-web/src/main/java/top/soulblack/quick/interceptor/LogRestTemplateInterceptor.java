package top.soulblack.quick.interceptor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Slf4j
public class LogRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static final String BUFFERING_RESPONSE_CLASS_NAME = "org.springframework.http.client.BufferingClientHttpResponseWrapper";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long startTime = System.currentTimeMillis();
        long elapseTime;
        ClientHttpResponse response;
        try {
            response = execution.execute(request, body);
        } finally {
            elapseTime = System.currentTimeMillis();
        }
        HttpInfo httpInfo = getHttpInfo(request, response);
        if (body != null && body.length > 0) {
            httpInfo.setRequestBody(new String(body, StandardCharsets.UTF_8));
        }
        httpInfo.setCost(elapseTime - startTime);
        logHttpInfo(httpInfo);
        return response;
    }

    private HttpInfo getHttpInfo(HttpRequest request, ClientHttpResponse response) throws IOException {
        HttpInfo httpInfo = new HttpInfo();
        httpInfo.setMethod(request.getMethod().name());
        httpInfo.setUri(request.getURI().toString());
        httpInfo.setStatusCode(response.getRawStatusCode());
        httpInfo.setRequestHeaders(request.getHeaders().toString());
        httpInfo.setResponseHeaders(response.getHeaders().toString());
        httpInfo.setResponseBody(getResponseBody(response));
        return httpInfo;
    }

    private String getResponseBody(ClientHttpResponse response) throws IOException {
        if (response == null) {
            return null;
        }
        String responseClassName = response.getClass().getName();
        boolean isBufferingClientHttpResponseWrapper = responseClassName.equals(BUFFERING_RESPONSE_CLASS_NAME);
        if (!isBufferingClientHttpResponseWrapper) {
            return "Unable to display";
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        }
        return sb.toString();
    }

    private void logHttpInfo(HttpInfo httpInfo) {
        log.info("{} {} status {} cost {}ms. Request: {headers:{} body:{}} Response: {headers:{} body:{}}"
                , httpInfo.getMethod(), httpInfo.getUri(), httpInfo.getStatusCode(), httpInfo.getCost()
                , httpInfo.getRequestHeaders(), httpInfo.getRequestBody()
                , httpInfo.getResponseHeaders(), httpInfo.getResponseBody());
    }

    @Data
    private class HttpInfo {
        private String method;
        private String uri;
        private String requestHeaders;
        private String requestBody;
        private String responseHeaders;
        private String responseBody;
        private int statusCode;
        private long cost;
    }
}
