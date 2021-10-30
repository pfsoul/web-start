package top.soulblack.quick.common.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Slf4j
@Component
public class HttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    public <RESP> RESP doGet(String url, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode, Object... uriVariables) {
        return exchange(restTemplate, url, HttpMethod.GET, null, null, responseType, systemCode, uriVariables);
    }

    public static <RESP> RESP doGet(RestTemplate restTemplate, String url, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode, Object... uriVariables) {
        return exchange(restTemplate, url, HttpMethod.GET, null, null, responseType, systemCode, uriVariables);
    }

    public static <RESP> RESP doGet(RestTemplate restTemplate, String url, HttpHeaders headers, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode, Object... uriVariables) {
        return exchange(restTemplate, url, HttpMethod.GET, headers, null, responseType, systemCode, uriVariables);
    }

    public <REQ, RESP> RESP doPost(String url, REQ req, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode) {
        return doPost(url, new HttpHeaders(), req, responseType, systemCode);
    }

    public <REQ, RESP> RESP doPost(String url, HttpHeaders headers, REQ req, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode) {
        return doPost(restTemplate, url, headers, req, responseType, systemCode);
    }

    public static <REQ, RESP> RESP doPost(RestTemplate restTemplate, String url, REQ req, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode) {
        return doPost(restTemplate, url, null, req, responseType, systemCode);
    }

    public static <REQ, RESP> RESP doPost(RestTemplate restTemplate, String url, HttpHeaders headers, REQ req, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode) {
        return exchange(restTemplate, url, HttpMethod.POST, headers, req, responseType, systemCode);
    }

    public static <REQ, RESP> RESP exchange(RestTemplate restTemplate, String url, HttpMethod httpMethod, HttpHeaders headers, REQ req, ParameterizedTypeReference<RESP> responseType, SystemCode systemCode, Object... uriVariables) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        HttpEntity<REQ> httpEntity = new HttpEntity<>(req, headers);
        ResponseEntity<RESP> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, responseType, uriVariables);
        } catch (Exception e) {
            log.error("call {} failed request: {}", url, JSONUtil.toJsonStr(req), e);
            throw new RuntimeException(systemCode.code);
        }
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            log.error("call {} error request: {}, response:{}", url, JSONUtil.toJsonStr(req),
                    JSONUtil.toJsonStr(responseEntity));
            throw new RuntimeException(systemCode.code);
        }
        return responseEntity.getBody();
    }

    public enum SystemCode {
        X5("X5"), PDD_EDI("PDD-EDI"), DES_INT("DES-INT");

        private String code;

        SystemCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
