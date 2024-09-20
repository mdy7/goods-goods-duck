package spharos.nu.goods.domain.goods.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.goods.entity.Goods;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long>, GoodsRepositoryCustom {
	Optional<Goods> findFirstByOrderByIdDesc();

	Optional<Goods> findOneByGoodsCode(String code);

	void deleteByGoodsCode(String code);

	List<Goods> findByTradingStatusAndClosedAtBefore(byte tradingStatus, LocalDateTime closedAt);

	List<Goods> findByTradingStatusAndOpenedAtBefore(byte b, LocalDateTime now);
}