package spharos.nu.aggregation.domain.wish.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spharos.nu.aggregation.domain.wish.dto.GoodsCodeDto;
import spharos.nu.aggregation.domain.wish.entity.Wish;

public interface WishRepositoryCustom {

	Page<GoodsCodeDto> findWishedGoodsByUuid(String uuid, Pageable pageable);
}
