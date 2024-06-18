package spharos.nu.read.domain.goods.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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

		Query query = new Query();

		// 기본 필터링 조건 추가
		query.addCriteria(Criteria.where("isDisable").is(false));

		// 키워드 검색 조건 추가
		Criteria keywordCriteria = new Criteria().orOperator(
			Criteria.where("name").regex(keyword, "i"), // 상품명에서 키워드 검색 (대소문자 구분 없음)
			Criteria.where("tagList").regex(keyword, "i") // 태그에서 키워드 검색 (대소문자 구분 없음)
		);
		query.addCriteria(keywordCriteria);

		// 거래 상태 필터링 조건 추가
		if (isTradingOnly) {
			query.addCriteria(Criteria.where("tradingStatus").is(1)); // 거래 중인 상태만 검색
		}

		// 정렬 조건 추가
		applySortCriteria(query, sort);

		// 페이지네이션 추가
		query.skip(pageable.getOffset());
		query.limit(pageable.getPageSize());

		// 검색 실행
		List<Goods> goodsList = mongoTemplate.find(query, Goods.class);
		long totalCount = mongoTemplate.count(query, Goods.class);

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

	private void applySortCriteria(Query query, String sort) {
		if (sort != null) {
			switch (sort) {
				case "createdAt":
					query.with(Sort.by(Sort.Direction.DESC, "createdAt")); // 최신순
					break;
				case "bidCount":
					query.with(Sort.by(Sort.Direction.DESC, "bidCount")); // 입찰 많은 순
					break;
				case "wishCount":
					query.with(Sort.by(Sort.Direction.DESC, "wishCount")); // 좋아요 많은 순
					break;
				case "endTime":
					query.with(Sort.by(Sort.Direction.ASC, "closedAt")); // 마감 임박 순
					break;
				default:
					query.with(Sort.by(Sort.Direction.DESC, "createdAt")); // 기본 정렬
					break;
			}
		}
	}
}
