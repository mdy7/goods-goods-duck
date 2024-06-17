package spharos.nu.read.domain.goods.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spharos.nu.read.domain.goods.dto.event.CountEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsCreateEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDeleteEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsDisableEventDto;
import spharos.nu.read.domain.goods.dto.event.GoodsStatusEventDto;
import spharos.nu.read.domain.goods.repository.ReadRepository;

@Service
@RequiredArgsConstructor
public class ReadService {

	private final ReadRepository readRepository;

	@Transactional
	public void createGoods(GoodsCreateEventDto goodsCreateEventDto) {
		//굿즈 저장
	}

	@Transactional
	public void deleteGoods(GoodsDeleteEventDto goodsDeleteEventDto) {
		//굿즈 삭제
	}

	@Transactional
	public void updateGoodsStatus(GoodsStatusEventDto goodsStatusEventDto) {
		//굿즈 상태 업데이트
	}

	@Transactional
	public void updateGoodsDisable(GoodsDisableEventDto goodsDisableEventDto) {
		//굿즈 숨김 업데이트
	}

	@Transactional
	public void updateWishCount(CountEventDto countEventDto) {
		//굿즈 종아요수 업데이트
	}

	@Transactional
	public void updateViewsCount(CountEventDto countEventDto) {
		//굿즈 조회수 업데이트
	}

	@Transactional
	public void updateBidCount(CountEventDto countEventDto) {
		//굿즈 입찰수 업데이트
	}
}
