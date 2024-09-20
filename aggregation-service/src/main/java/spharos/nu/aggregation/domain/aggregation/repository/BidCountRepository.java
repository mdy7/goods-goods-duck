package spharos.nu.aggregation.domain.aggregation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.aggregation.domain.aggregation.entity.BidCount;
import spharos.nu.aggregation.domain.aggregation.entity.WishCount;

@Repository
public interface BidCountRepository extends JpaRepository<BidCount, Long> {

	Optional<BidCount> findByGoodsCode(String goodsCode);

	void deleteAllByGoodsCode(String goodsCode);
}
