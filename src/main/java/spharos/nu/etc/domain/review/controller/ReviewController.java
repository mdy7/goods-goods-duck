package spharos.nu.etc.domain.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.service.ReviewService;
import spharos.nu.etc.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/etc/reviews")
@Tag(name = "Review", description = "etc-service에서 리뷰 관련 API document")
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping("")
	@Operation(summary = "받은 거래 후기 조회", description = "")
	public ResponseEntity<ApiResponse<Void>> getReviews(
		@RequestHeader(value = "User-Uuid", required = false) String receiverUuid) {

		return ApiResponse.success(reviewService.reviewsGet(receiverUuid), "받은 거래 후기 조회 성공");
	}
}
