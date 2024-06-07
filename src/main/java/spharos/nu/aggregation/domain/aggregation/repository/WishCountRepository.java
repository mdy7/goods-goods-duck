package spharos.nu.aggregation.domain.aggregation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spharos.nu.aggregation.domain.aggregation.entity.WishCount;

import java.util.Optional;

public interface WishCountRepository extends JpaRepository<WishCount, Long> {
    Optional<WishCount> findByGoodsCode(String goodsCode);
}
