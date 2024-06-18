package spharos.nu.read.domain.goods.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.GoodsDetailDto;
import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.entity.Goods;
import spharos.nu.read.domain.goods.repository.ReadRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationService {

	private final ReadRepository readRepository;

	public Long getWishCount(String goodsCode) {

		Goods goods = readRepository.findByGoodsCode(goodsCode).orElseThrow();
		return goods.getWishCount();
	}

	public Long getViewsCount(String goodsCode) {

		Goods goods = readRepository.findByGoodsCode(goodsCode).orElseThrow();
		return goods.getViewsCount();
	}

	public Long getBidCount(String goodsCode) {

		Goods goods = readRepository.findByGoodsCode(goodsCode).orElseThrow();
		return goods.getBidCount();
	}
}
