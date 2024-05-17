package spharos.nu.goods.domain.goods.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.goods.entity.Goods;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long>, GoodsRepositoryCustom {
	Optional<Goods> findFirstByOrderByIdDesc();

	Optional<Goods> findOneByCode(String code);

	void deleteByCode(String code);
}