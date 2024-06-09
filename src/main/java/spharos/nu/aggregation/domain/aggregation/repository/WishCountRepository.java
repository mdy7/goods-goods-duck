package spharos.nu.aggregation.domain.aggregation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.aggregation.domain.aggregation.entity.WishCount;

@Repository
public interface WishCountRepository extends JpaRepository<WishCount, Long> {

	Optional<WishCount> findByGoodsCode(String goodsCode);

}
