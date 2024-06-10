package spharos.nu.etc.domain.review.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/etc/reviews")
@Tag(name = "Review", description = "etc-service에서 리뷰 관련 API document")
public class ReviewController {

}
