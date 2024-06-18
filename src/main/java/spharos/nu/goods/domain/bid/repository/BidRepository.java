package spharos.nu.goods.domain.bid.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.bid.entity.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long>, BidRepositoryCustom {

	List<Bid> findByGoodsCode(String goodsCode);

	List<Bid> findByGoodsCodeAndCreatedAtBeforeOrderByPriceDescCreatedAtAsc(String goodsCode,
		LocalDateTime closedAt);

	@Query("SELECT DISTINCT b.bidderUuid FROM Bid b WHERE b.goodsCode = :goodsCode")
	List<String> findDistinctBiddersByGoodsCode(String goodsCode);
}
