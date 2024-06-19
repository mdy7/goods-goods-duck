package spharos.nu.goods.domain.bid.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.bid.entity.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long>, BidRepositoryCustom {

	List<Bid> findByGoodsCode(String goodsCode);



	@Query("SELECT DISTINCT b.bidderUuid FROM Bid b WHERE b.goodsCode = :goodsCode")
	List<String> findDistinctBiddersByGoodsCode(String goodsCode);

	Optional<Bid> findFirstByGoodsCodeOrderByPriceDescCreatedAtAsc(String goodsCode);
}
