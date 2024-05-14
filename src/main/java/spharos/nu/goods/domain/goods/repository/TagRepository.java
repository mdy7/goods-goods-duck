package spharos.nu.goods.domain.goods.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	List<Tag> findAllByCode(String code);
}