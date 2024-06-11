package spharos.nu.etc.domain.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.dto.request.ReviewRequestDto;
import spharos.nu.etc.domain.review.service.ReviewService;
import spharos.nu.etc.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/etc/reviews")
@Tag(name = "Review", description = "etc-service에서 리뷰 관련 API document")
public class ReviewController {

	private final ReviewService reviewService;

	// 후기 작성
	@PostMapping("")
	@Operation(summary = "거래완료 후 후기 작성", description = "거래완료 후 후기작성시 후기 저장")
	public ResponseEntity<ApiResponse<Void>> createReview(@RequestHeader(value = "User-Uuid", required = false) String writerUuid,
		@RequestBody ReviewRequestDto reviewRequestDto) {

		String receiverUuid;

		if (writerUuid.equals(reviewRequestDto.getBidderUuid())) {
			receiverUuid = reviewRequestDto.getSellerUuid();
		} else {
			receiverUuid = reviewRequestDto.getBidderUuid();
		}

		return ApiResponse.success(reviewService.reviewCreate(writerUuid, receiverUuid, reviewRequestDto), "후기 작성 완료");
	}
}
