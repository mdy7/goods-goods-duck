package spharos.nu.aggregation.domain.wish.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.aggregation.domain.wish.dto.GoodsCodeDto;
import spharos.nu.aggregation.domain.wish.dto.WishGoodsResponseDto;
import spharos.nu.aggregation.domain.wish.repository.WishRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

	private final WishRepository wishRepository;

	public WishGoodsResponseDto wishGoodsGet(String uuid, Integer index) {

		Pageable pageable = PageRequest.of(index, 10);
		Page<GoodsCodeDto> goodsCodePage = wishRepository.findWishedGoodsByUuid(uuid, pageable);

		return WishGoodsResponseDto.builder()
			.totalCount(goodsCodePage.getTotalElements())
			.nowPage(goodsCodePage.getNumber())
			.maxPage(goodsCodePage.getTotalPages())
			.isLast(goodsCodePage.isLast())
			.goodsList(goodsCodePage.getContent())
			.build();
	}
}
