package spharos.nu.goods.domain.goods.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.goods.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findAllByGoodsCode(String code);
	void deleteAllByGoodsCode(String code);
}