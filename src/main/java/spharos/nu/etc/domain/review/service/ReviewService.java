package spharos.nu.etc.domain.review.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public Void reviewsGet(String receiverUuid) {

		return null;
	}
}
