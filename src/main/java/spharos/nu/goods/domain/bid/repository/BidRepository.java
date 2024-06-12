package spharos.nu.goods.domain.bid.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spharos.nu.goods.domain.goods.dto.GoodsCodeDto;

import spharos.nu.goods.domain.bid.entity.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

	List<Bid> findByGoodsCode(String goodsCode);

	List<Bid> findByGoodsCodeAndCreatedAtBeforeOrderByPriceDescCreatedAtAsc(String goodsCode,
                                                                            LocalDateTime closedAt);

	Page<GoodsCodeDto> findByBidderUuidOrderByCreatedAtDesc(String bidderUuid, Pageable pageable);
}
