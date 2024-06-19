package spharos.nu.read.domain.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.read.domain.goods.dto.response.SearchResultResponseDto;
import spharos.nu.read.domain.goods.entity.Goods;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SearchService {

	private final MongoTemplate mongoTemplate;
	// private final ReadRepository readRepository;

	public SearchResultResponseDto searchResultGet(String keyword, String sort, boolean isTradingOnly,
		Pageable pageable) {

		List<AggregationOperation> operations = new ArrayList<>();

		// 기본 필터링 조건 추가
		MatchOperation basicFilter = Aggregation.match(Criteria.where("isDisable").is(false));
		operations.add(basicFilter);

		// 키워드 검색 조건 추가
		Criteria keywordCriteria = new Criteria().orOperator(
			Criteria.where("name").regex(keyword, "i"), // 상품명에서 키워드 검색 (대소문자 구분 없음)
			Criteria.where("tagList").regex(keyword, "i") // 태그에서 키워드 검색 (대소문자 구분 없음)
		);
		MatchOperation keywordFilter = Aggregation.match(keywordCriteria);
		operations.add(keywordFilter);

		// 거래 상태 필터링 조건 추가
		if (isTradingOnly) {
			MatchOperation tradingFilter = Aggregation.match(Criteria.where("tradingStatus").in((byte) 0, (byte) 1)); // 거래 중인 상태(0 또는 1)만 검색
			operations.add(tradingFilter);
		}

		// 데이터가 여기까지 제대로 있는지 확인
		Aggregation debugAggregation = Aggregation.newAggregation(operations);
		List<Goods> debugGoodsList = mongoTemplate.aggregate(debugAggregation, "goods", Goods.class).getMappedResults();
		log.info("After basic filters and keyword search: {}", debugGoodsList);

		GroupOperation groupOperation = Aggregation
			.group("goodsCode")
			.first("goodsCode").as("goodsCode")
			.first("name").as("name")
			.first("minPrice").as("minPrice")
			.first("openedAt").as("openedAt")
			.first("closedAt").as("closedAt")
			.first("tradingStatus").as("tradingStatus")
			.first("createdAt").as("createdAt")
			.first("bidCount").as("bidCount")
			.first("wishCount").as("wishCount")
			.first("viewsCount").as("viewsCount");

		SortOperation sortOperation = applySortCriteria(sort);

		operations.add(groupOperation);
		operations.add(sortOperation);
		operations.add(Aggregation.skip(pageable.getOffset()));
		operations.add(Aggregation.limit(pageable.getPageSize()));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		// 검색 실행
		AggregationResults<Goods> aggregationResults = mongoTemplate.aggregate(aggregation, "goods", Goods.class);
		List<Goods> goodsList = aggregationResults.getMappedResults();

		// 데이터가 제대로 들어왔는지 확인
		log.info("Final goods list: {}", goodsList);

		// 필터링 조건을 사용하여 총 개수 계산
		Query countQuery = new Query();
		countQuery.addCriteria(Criteria.where("isDisable").is(false));
		countQuery.addCriteria(keywordCriteria);
		if (isTradingOnly) {
			countQuery.addCriteria(Criteria.where("tradingStatus").is(1));
		}
		long totalCount = mongoTemplate.count(countQuery, Goods.class);

		List<GoodsSummaryDto> goodsSummaryList = goodsList.stream()
			.map(goods -> GoodsSummaryDto.builder()
				.goodsCode(goods.getGoodsCode())
				.goodsName(goods.getName())
				.minPrice(goods.getMinPrice())
				.openedAt(goods.getOpenedAt())
				.closedAt(goods.getClosedAt())
				.tradingStatus(goods.getTradingStatus())
				.build())
			.toList();

		int totalPages = (int)Math.ceil((double)totalCount / pageable.getPageSize());
		boolean isLast = pageable.getPageNumber() + 1 >= totalPages;

		return SearchResultResponseDto.builder()
			.totalCount(totalCount)
			.nowPage(pageable.getPageNumber())
			.maxPage(totalPages)
			.isLast(isLast)
			.goodsList(goodsSummaryList)
			.build();
	}

	private static SortOperation applySortCriteria(String sort) {
		if (sort != null) {
			switch (sort) {
				case "createdAt":
					return Aggregation.sort(Sort.Direction.DESC, "createdAt"); // 최신순
				case "bidCount":
					return Aggregation.sort(Sort.Direction.DESC, "bidCount"); // 입찰 많은 순
				case "wishCount":
					return Aggregation.sort(Sort.Direction.DESC, "wishCount"); // 좋아요 많은 순
				case "endTime":
					return Aggregation.sort(Sort.Direction.ASC, "closedAt"); // 마감 임박 순
				default:
					return Aggregation.sort(Sort.Direction.DESC, "createdAt"); // 기본 정렬
			}
		}
		return Aggregation.sort(Sort.Direction.DESC, "createdAt"); // 기본 정렬
	}
}
