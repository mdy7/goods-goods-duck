package spharos.nu.goods.domain.goods.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.goods.dto.GoodsCodeDto;
import spharos.nu.goods.domain.goods.dto.GoodsSellResponseDto;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final GoodsRepository goodsRepository;

	public GoodsSellResponseDto sellGoodsGet(String uuid, Integer index, byte statusNum) {

		Pageable pageable = PageRequest.of(index, 10);
		Page<GoodsCodeDto> goodsCodePage = goodsRepository.findAllGoods(uuid, statusNum, pageable);

		return GoodsSellResponseDto.builder()
			.totalCount(goodsCodePage.getTotalElements())
			.nowPage(goodsCodePage.getNumber())
			.maxPage(goodsCodePage.getTotalPages())
			.isLast(goodsCodePage.isLast())
			.goodsList(goodsCodePage.getContent())
			.build();
	}

}
