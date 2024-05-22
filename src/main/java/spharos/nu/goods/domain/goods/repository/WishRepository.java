package spharos.nu.goods.domain.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.goods.domain.goods.entity.Wish;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
}
