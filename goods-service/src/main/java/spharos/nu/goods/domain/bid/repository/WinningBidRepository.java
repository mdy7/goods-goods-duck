package spharos.nu.goods.domain.bid.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.bid.entity.WinningBid;

@Repository
public interface WinningBidRepository extends JpaRepository<WinningBid, Long>, WinningBidRepositoryCustom {
	Optional<WinningBid> findByGoodsCode(String goodsCode);
}
