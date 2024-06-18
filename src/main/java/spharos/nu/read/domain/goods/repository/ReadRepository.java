package spharos.nu.read.domain.goods.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.entity.Goods;

@Repository
public interface ReadRepository extends MongoRepository<Goods, String> {

	Optional<Goods> findByGoodsCode(String goodsCode);

	Page<GoodsSummaryDto> findGoodsBySellerUuidAndTradingStatusOrderByCreatedAtDesc(String sellerUuid,
		Pageable pageable, byte status);

}
