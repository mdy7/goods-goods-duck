package spharos.nu.aggregation.domain.aggregation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spharos.nu.aggregation.domain.aggregation.entity.BidCount;
import spharos.nu.aggregation.domain.aggregation.entity.ViewsCount;
import spharos.nu.aggregation.domain.aggregation.repository.BidCountRepository;
import spharos.nu.aggregation.domain.aggregation.repository.ViewsCountRepository;

@Service
@RequiredArgsConstructor
public class AggregationService {

	private final ViewsCountRepository viewsCountRepository;
	private final BidCountRepository bidCountRepository;

	@Transactional
	public void addViewsCount(String goodsCode) {

		ViewsCount viewsCount = viewsCountRepository.findByGoodsCode(goodsCode).orElseThrow();
		viewsCount.increaseViewsCount();

	}

	@Transactional
	public void addBidCount(String goodsCode) {

		BidCount bidCount = bidCountRepository.findByGoodsCode(goodsCode).orElseThrow();
		bidCount.increaseBidCount();

	}

}
