package top.soulblack.quick.log;

import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static feign.Util.decodeOrDefault;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Slf4j
public class FeignLog extends feign.Logger {

    static ThreadLocal<Map<String, String>> logContext = new ThreadLocal();
    static String PATH = "path";
    static String METHOD = "method";
    static String REQUEST_HEADER = "request_header";
    static String REQUEST_BODY = "request_body";
    static String RESPOND_BODY = "respond_body";

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        Map<String, String> logMap = new HashMap<>();
        logMap.put(PATH, request.url());
        logMap.put(METHOD, String.valueOf(request.httpMethod()));
        logMap.put(REQUEST_HEADER, request.headers().toString());
        logMap.put(REQUEST_BODY, request.body() == null ? null :
                request.charset() == null ? null : new String(request.body(), request.charset()));
        logContext.set(logMap);
    }

    @Override
    protected Response logAndRebufferResponse(
            String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        Map<String, String> requestParam = logContext.get();
        logContext.remove();
        // 返回参数
        if (response.body() != null && !(response.status() == 204 || response.status() == 205)) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            if (bodyData.length > 0) {
                String responseBody = decodeOrDefault(bodyData, UTF_8, "Binary data");
                requestParam.put(RESPOND_BODY, responseBody.replaceAll("\\s*|\t|\r|\n", ""));
            }
            log.info("METHOD :{}, {}, PATH :{}, {}ms, REQUEST HEADER:{}, REQUEST BODY:{}, RESPOND BODY:{}",
                    requestParam.get(METHOD), response.status(), requestParam.get(PATH), elapsedTime,
                    requestParam.get(REQUEST_HEADER), requestParam.get(REQUEST_BODY), requestParam.get(RESPOND_BODY));
            return response.toBuilder().body(bodyData).build();
        }
        log.info("METHOD :{}, {}, PATH :{}, {}ms, REQUEST HEADER:{}, REQUEST BODY:{}, RESPOND BODY:{}",
                requestParam.get(METHOD), response.status(), requestParam.get(PATH), elapsedTime,
                requestParam.get(REQUEST_HEADER), requestParam.get(REQUEST_BODY), requestParam.get(RESPOND_BODY));
        return response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (log.isInfoEnabled()) {
            log.info(String.format(methodTag(configKey) + format, args));
        }
    }
}
