package spharos.nu.aggregation.domain.aggregation.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spharos.nu.aggregation.domain.aggregation.entity.WishCount;
import spharos.nu.aggregation.domain.aggregation.repository.WishCountRepository;

@Service
@RequiredArgsConstructor
public class AggregationService {

	private final WishCountRepository wishCountRepository;

	public Long getWishCount(String goodsCode) {
		return wishCountRepository.findByGoodsCode(goodsCode)
			.map(WishCount::getCount)
			.orElse(0L);
	}

}
