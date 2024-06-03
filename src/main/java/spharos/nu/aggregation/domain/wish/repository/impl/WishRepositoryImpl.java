package spharos.nu.aggregation.domain.wish.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spharos.nu.aggregation.domain.wish.dto.GoodsCodeDto;
import spharos.nu.aggregation.domain.wish.dto.QGoodsCodeDto;
import spharos.nu.aggregation.domain.wish.entity.QWish;
import spharos.nu.aggregation.domain.wish.repository.WishRepositoryCustom;

@RequiredArgsConstructor
public class WishRepositoryImpl implements WishRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GoodsCodeDto> findWishedGoodsByUuid(String uuid, Pageable pageable) {

		QWish wish = QWish.wish;

		List<GoodsCodeDto> goodsList = queryFactory
			.select(new QGoodsCodeDto(wish.goodsCode))
			.from(wish)
			.where(wish.uuid.eq(uuid))
			.orderBy(wish.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(wish.count())
			.from(wish)
			.where(wish.uuid.eq(uuid))
			.fetchOne();

		long totalCount = total != null ? total : 0;

		return new PageImpl<>(goodsList, pageable, totalCount);
	}
}
