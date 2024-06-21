package spharos.nu.member.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spharos.nu.member.domain.payment.dto.KakaoPayReadyRequestDto;
import spharos.nu.member.domain.payment.dto.KakaoPayReadyResponseDto;
import spharos.nu.member.domain.payment.service.KakaoPayService;
import spharos.nu.member.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users-n")
@Slf4j
@Tag(name = "Pay", description = "결제 관련 API document")
public class KaKaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/pay/ready")
    @Operation(summary = "카카오페이 결제 준비", description = "카카오페이 결제 준비")
    public KakaoPayReadyResponseDto payReady(@RequestBody KakaoPayReadyRequestDto kakaoPayReadyRequestDto
    ) {
        KakaoPayReadyResponseDto readyResponse = kakaoPayService.payReady(kakaoPayReadyRequestDto);
        return readyResponse;
    }

    @GetMapping("/pay/completed")
    public ResponseEntity<ApiResponse<Void>> payCompleted(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("uuid") String uuid
    ) {
        kakaoPayService.payApprove(pgToken, uuid);
        return ApiResponse.success(null, "결제가 완료되었습니다.");
    }
}
