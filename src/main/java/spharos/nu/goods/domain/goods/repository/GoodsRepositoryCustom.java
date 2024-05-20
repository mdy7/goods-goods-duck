package spharos.nu.goods.domain.goods.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spharos.nu.goods.domain.goods.dto.GoodsSummaryDto;
import spharos.nu.goods.domain.goods.entity.Goods;

public interface GoodsRepositoryCustom {
	Page<GoodsSummaryDto> findAllGoods(Long categoryPk, boolean isTradingOnly, Pageable pageable);
}
