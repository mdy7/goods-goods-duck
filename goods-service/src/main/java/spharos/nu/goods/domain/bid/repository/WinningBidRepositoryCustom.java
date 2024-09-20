package spharos.nu.goods.domain.bid.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spharos.nu.goods.domain.bid.dto.response.BidGoodsCodeDto;

public interface WinningBidRepositoryCustom {

	Page<BidGoodsCodeDto> findAllGoods(String uuid, Pageable pageable, byte status);
}
