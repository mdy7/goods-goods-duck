package spharos.nu.aggregation.domain.aggregation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.aggregation.domain.aggregation.dto.event.CountEventDto;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsCreateEventDto;
import spharos.nu.aggregation.domain.aggregation.dto.event.GoodsDeleteEventDto;
import spharos.nu.aggregation.domain.aggregation.entity.BidCount;
import spharos.nu.aggregation.domain.aggregation.entity.ViewsCount;
import spharos.nu.aggregation.domain.aggregation.entity.WishCount;
import spharos.nu.aggregation.domain.aggregation.kafka.KafkaProducer;
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
	private final KafkaProducer kafkaProducer;

	@Transactional
	public void addViewsCount(String goodsCode) {

		/* 상품의 조회수 찾기 */
		ViewsCount viewsCount = viewsCountRepository.findByGoodsCode(goodsCode)
			.orElseGet(() -> viewsCountRepository.save(ViewsCount.builder()
				.goodsCode(goodsCode)
				.count(0L)
				.build()));

		/* 업데이트해서 저장 */
		ViewsCount updatedViewsCount = viewsCountRepository.save(ViewsCount.builder()
			.id(viewsCount.getId())
			.goodsCode(viewsCount.getGoodsCode())
			.count(viewsCount.getCount() + 1) //숫자 업데이트
			.build());

		/* new 조회수를 리드서버로 전송 */
		kafkaProducer.sendViewsCountEvent(CountEventDto.builder()
			.goodsCode(updatedViewsCount.getGoodsCode())
			.count(updatedViewsCount.getCount())
			.build());

		log.info("(goodsCode: {}) 조회수 업데이트 이벤트 발행", updatedViewsCount.getGoodsCode());
	}

	@Transactional
	public void addBidCount(String goodsCode) {

		/* 상품의 입찰수 찾기 */
		BidCount bidCount = bidCountRepository.findByGoodsCode(goodsCode)
			.orElseGet(() -> bidCountRepository.save(BidCount.builder()
					.goodsCode(goodsCode)
					.count(0L)
					.build()));

		/* 업데이트해서 저장 */
		BidCount updatedBidCount = bidCountRepository.save(BidCount.builder()
			.id(bidCount.getId())
			.goodsCode(bidCount.getGoodsCode())
			.count(bidCount.getCount() + 1) //숫자 업데이트
			.build());

		/* new 입찰수를 리드서버로 전송 */
		kafkaProducer.sendBidCountEvent(CountEventDto.builder()
			.goodsCode(updatedBidCount.getGoodsCode())
			.count(updatedBidCount.getCount())
			.build());

		log.info("(goodsCode: {}) 입찰수 업데이트 이벤트 발행", updatedBidCount.getGoodsCode());

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
		log.info("(goodsCode: {}) 좋아요수,조회수,입찰수 각각 객체 생성", goodsCreateEventDto.getGoodsCode());

	}

	@Transactional
	public void deleteAggregation(GoodsDeleteEventDto goodsDeleteEventDto) {

		log.info("goods-delete-topic 에서 이벤트 수신");

		wishCountRepository.deleteAllByGoodsCode(goodsDeleteEventDto.getGoodsCode());
		viewsCountRepository.deleteAllByGoodsCode(goodsDeleteEventDto.getGoodsCode());
		bidCountRepository.deleteAllByGoodsCode(goodsDeleteEventDto.getGoodsCode());

		log.info("(goodsCode: {}) 좋아요수,조회수,입찰수 각각 객체 삭제", goodsDeleteEventDto.getGoodsCode());

	}

}
