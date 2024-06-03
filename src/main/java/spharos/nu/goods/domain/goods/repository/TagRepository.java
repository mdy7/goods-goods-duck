package spharos.nu.goods.domain.goods.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.goods.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	List<Tag> findAllByGoodsCode(String code);

	void deleteAllByGoodsCode(String code);
}