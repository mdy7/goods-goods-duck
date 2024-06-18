package spharos.nu.goods.domain.bid.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.bid.entity.WinningBid;
import spharos.nu.goods.domain.goods.dto.response.GoodsCodeDto;

@Repository
public interface WinningBidRepository extends JpaRepository<WinningBid, Long>, WinningBidRepositoryCustom {
	Optional<WinningBid> findByGoodsCode(String goodsCode);
}
