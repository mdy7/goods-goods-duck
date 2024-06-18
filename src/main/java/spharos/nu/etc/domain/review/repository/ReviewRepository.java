package spharos.nu.etc.domain.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.etc.domain.review.dto.response.ReviewListDto;
import spharos.nu.etc.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	Page<Review> findByReceiverUuidOrderByCreatedAtDesc(String receiverUuid, Pageable pageable);

	Optional<Review> findByWriterUuidAndGoodsCode(String writerUuid, String goodsCode);
}
