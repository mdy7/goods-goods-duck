package spharos.nu.goods.domain.goods.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spharos.nu.goods.domain.goods.dto.response.GoodsCodeDto;

public interface GoodsRepositoryCustom {
	Page<GoodsCodeDto> findAllGoods(Long categoryPk, boolean isTradingOnly, Pageable pageable);

	Page<GoodsCodeDto> findAllGoods(String uuid, byte status, Pageable pageable);

}
