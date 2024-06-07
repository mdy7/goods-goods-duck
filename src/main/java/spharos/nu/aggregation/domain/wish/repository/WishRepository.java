package spharos.nu.aggregation.domain.wish.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.aggregation.domain.wish.entity.Wish;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long>, WishRepositoryCustom {

	Optional<Wish> findByUuidAndGoodsCode(String uuid, String goodsCode);

}
