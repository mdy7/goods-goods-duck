package spharos.nu.member.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import spharos.nu.member.domain.payment.dto.ApproveResponseDto;
import spharos.nu.member.domain.payment.dto.KakaoPayReadyRequestDto;
import spharos.nu.member.domain.payment.dto.KakaoPayReadyResponseDto;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class KakaoPayService {

    @Value("${kakao_pay_secret_key}")
    private String secretKey;

    private final RedisTemplate<String, String> redisTemplate;

    private final DuckPointService duckPointService;


    /**
     * 카카오 페이 결제창 연결
     */
    public KakaoPayReadyResponseDto payReady(KakaoPayReadyRequestDto kakaoPayReadyRequestDto) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
        parameters.put("partner_order_id", "1234567890");                       // 주문번호
        parameters.put("partner_user_id", kakaoPayReadyRequestDto.getUuid());   // 회원 아이디
        parameters.put("item_name", kakaoPayReadyRequestDto.getItemName());     // 상품명
        parameters.put("quantity", "1");                                        // 상품 수량
        parameters.put("total_amount", String.valueOf(kakaoPayReadyRequestDto.getTotalAmount()));   // 상품 총액
        parameters.put("tax_free_amount", "0");                                 // 상품 비과세 금액
        parameters.put("approval_url", "https://goodsgoodsduck.shop/api/v1/users-n/pay/completed?uuid=" + kakaoPayReadyRequestDto.getUuid()); // 결제 성공 시 URL
        parameters.put("cancel_url", "https://goodsgoodsduck.shop");            // 결제 취소 시 URL
        parameters.put("fail_url", "https://goodsgoodsduck.shop");              // 결제 실패 시 URL

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

        ResponseEntity<KakaoPayReadyResponseDto> responseEntity = template.postForEntity(url, requestEntity, KakaoPayReadyResponseDto.class);
        log.info("결제준비 응답객체: " + responseEntity.getBody());

        redisTemplate.opsForValue().set(kakaoPayReadyRequestDto.getUuid(), responseEntity.getBody().getTid());

        return responseEntity.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "DEV_SECRET_KEY " + secretKey);
        headers.set("Content-type", "application/json");
        return headers;
    }


    /**
     * 카카오페이 결제 승인
     */
    @Transactional
    public ApproveResponseDto payApprove(String pgToken, String uuid) {
        String tid = redisTemplate.opsForValue().get(uuid);
        log.info("tid: {}", tid);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
        parameters.put("tid", tid);                       // 결제 고유번호
        parameters.put("partner_order_id", "1234567890"); // 주문번호
        parameters.put("partner_user_id", uuid);    // 회원 아이디
        parameters.put("pg_token", pgToken);              // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        ApproveResponseDto approveResponse = template.postForObject(url, requestEntity, ApproveResponseDto.class);
        log.info("결제승인 응답객체: " + approveResponse);

        duckPointService.updatePoint(approveResponse);

        return approveResponse;
    }
}