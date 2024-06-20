package spharos.nu.aggregation.domain.aggregation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.aggregation.domain.aggregation.entity.BidCount;
import spharos.nu.aggregation.domain.aggregation.entity.ViewsCount;

@Repository
public interface ViewsCountRepository extends JpaRepository<ViewsCount, Long> {

	Optional<ViewsCount> findByGoodsCode(String goodsCode);

	void deleteAllByGoodsCode(String goodsCode);

}
