package spharos.nu.member.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spharos.nu.member.domain.payment.dto.ApproveResponseDto;
import spharos.nu.member.domain.payment.dto.KakaoPayReadyRequestDto;
import spharos.nu.member.domain.payment.dto.KakaoPayReadyResponseDto;
import spharos.nu.member.domain.payment.service.DuckPointService;
import spharos.nu.member.domain.payment.service.KakaoPayService;
import spharos.nu.member.global.apiresponse.ApiResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users-n")
@Slf4j
@Tag(name = "Pay", description = "결제 관련 API document")
public class KaKaoPayController {

    private final KakaoPayService kakaoPayService;
    private final DuckPointService duckPointService;

    @PostMapping("/pay/ready")
    @Operation(summary = "카카오페이 결제 준비", description = "카카오페이 결제 준비")
    public KakaoPayReadyResponseDto payReady(@RequestBody KakaoPayReadyRequestDto kakaoPayReadyRequestDto
    ) {
        KakaoPayReadyResponseDto readyResponse = kakaoPayService.payReady(kakaoPayReadyRequestDto);
        return readyResponse;
    }

    @GetMapping("/pay/completed")
    @Operation(summary = "카카오페이 결제 완료", description = "카카오페이 결제 완료, Redirect Url")
    public ResponseEntity<ApiResponse<Void>> payCompleted(
            HttpServletResponse response,
            @RequestParam("pg_token") String pgToken,
            @RequestParam("uuid") String uuid,
            @RequestParam("order_id") String order_id
    ) throws IOException {
        ApproveResponseDto approveResponseDto = kakaoPayService.payApprove(pgToken, uuid, order_id);
        try {
            duckPointService.updatePoint(approveResponseDto);
            response.sendRedirect("https://goodsgoodsduck.shop/mypage");
        } catch (Exception e) {
            log.error("포인트 충전 에러: {}", e.getMessage());
            // 포인트 충전 실패 시 결제 취소
            kakaoPayService.payCancel(approveResponseDto.getTid(), String.valueOf(approveResponseDto.getAmount().getTotal()));
            response.sendRedirect("https://goodsgoodsduck.shop/mypage");
            return ApiResponse.fail(null, "포인트 충전에 실패하였습니다.");

        }
        return ApiResponse.success(null, "결제가 완료되었습니다.");
    }


}
