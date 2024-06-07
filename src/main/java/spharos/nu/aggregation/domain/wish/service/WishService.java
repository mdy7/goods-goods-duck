package spharos.nu.aggregation.domain.wish.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.aggregation.domain.wish.dto.GoodsCodeDto;
import spharos.nu.aggregation.domain.wish.dto.WishGoodsResponseDto;
import spharos.nu.aggregation.domain.wish.entity.Wish;
import spharos.nu.aggregation.domain.wish.repository.WishRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishService {

	private final WishRepository wishRepository;

	public boolean getIsWish(String uuid, String goodsCode) {

		Optional<Wish> optionalWish = wishRepository.findByUuidAndGoodsCode(uuid, goodsCode);

		return optionalWish.isPresent();
	}
}
