package spharos.nu.aggregation.domain.aggregation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsCreateEventDto;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsDeleteEventDto;
import spharos.nu.aggregation.domain.aggregation.entity.BidCount;
import spharos.nu.aggregation.domain.aggregation.entity.ViewsCount;
import spharos.nu.aggregation.domain.aggregation.entity.WishCount;
import spharos.nu.aggregation.domain.aggregation.repository.BidCountRepository;
import spharos.nu.aggregation.domain.aggregation.repository.ViewsCountRepository;
import spharos.nu.aggregation.domain.aggregation.repository.WishCountRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationService {

	private final WishCountRepository wishCountRepository;
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

	@Transactional
	public void createAggregation(GoodsCreateEventDto goodsCreateEventDto) {

		log.info("goods-create-topic 에서 이벤트 수신");

		wishCountRepository.save(
			WishCount.builder()
				.goodsCode(goodsCreateEventDto.getGoodsCode())
				.count(0L)
				.build()
		);

		viewsCountRepository.save(
			ViewsCount.builder()
				.goodsCode(goodsCreateEventDto.getGoodsCode())
				.count(0L)
				.build()
		);

	    bidCountRepository.save(
			BidCount.builder()
				.goodsCode(goodsCreateEventDto.getGoodsCode())
				.count(0L)
				.build()
		);
		log.info("(goodsCode: {}) 좋아요수,조회수,입찰수 각각 객체 생성",goodsCreateEventDto.getGoodsCode());

	}

	@Transactional
	public void deleteAggregation(GoodsDeleteEventDto goodsDeleteEventDto) {

		log.info("goods-delete-topic 에서 이벤트 수신");

		wishCountRepository.deleteAllByGoodsCode(goodsDeleteEventDto.getGoodsCode());
		viewsCountRepository.deleteAllByGoodsCode(goodsDeleteEventDto.getGoodsCode());
		bidCountRepository.deleteAllByGoodsCode(goodsDeleteEventDto.getGoodsCode());

		log.info("(goodsCode: {}) 좋아요수,조회수,입찰수 각각 객체 삭제",goodsDeleteEventDto.getGoodsCode());

	}

}
