package spharos.nu.goods.domain.goods.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spharos.nu.goods.domain.goods.dto.GoodsCodeDto;
import spharos.nu.goods.domain.goods.dto.GoodsInfoDto;

public interface GoodsRepositoryCustom {
	Page<GoodsCodeDto> findAllGoods(Long categoryPk, boolean isTradingOnly, Pageable pageable);

	Page<GoodsInfoDto> findAllGoods(String uuid, byte statusNum, Pageable pageable);

}
