package spharos.nu.read.domain.goods.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.read.domain.goods.entity.Goods;

@Repository
public interface ReadRepository extends MongoRepository<Goods, String> {

	Optional<Goods> findByGoodsCode(String goodsCode);

	Page<Goods> findGoodsBySellerUuidAndTradingStatusAndIsDisableOrderByCreatedAtDesc(String sellerUuid,
		Pageable pageable, byte status, Boolean isDisable);

	Page<Goods> findByCategoryIdAndTradingStatusInAndIsDisable(Long categoryId, List<Byte> tradingStatus,
		Boolean isDisable, Pageable pageable);

	List<Goods> findByCategoryIdAndOpenedAtBeforeAndClosedAtAfter(Long categoryId, LocalDateTime now1, LocalDateTime now2, Pageable pageable);

    List<Goods> findByCategoryIdAndOpenedAtBetween(Long categoryId, LocalDateTime startOfTomorrow, LocalDateTime endOfTomorrow, Pageable pageable);
}
