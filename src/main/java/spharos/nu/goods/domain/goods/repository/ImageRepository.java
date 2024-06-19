package spharos.nu.goods.domain.goods.repository;

import java.util.List;
import java.util.Optional;

import org.bouncycastle.crypto.agreement.srp.SRP6Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.goods.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	Optional<Image> findByGoodsCodeAndIndex(String goodsCode, Integer index);
	List<Image> findAllByGoodsCode(String goodsCode);
	void deleteAllByGoodsCode(String goodsCode);
}