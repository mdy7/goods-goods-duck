package spharos.nu.aggregation.domain.wish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.aggregation.domain.wish.entity.Wish;

import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long>, WishRepositoryCustom {
    Optional<Wish> findByGoodsCodeAndUuid(String goodsCode, String uuid);
}
