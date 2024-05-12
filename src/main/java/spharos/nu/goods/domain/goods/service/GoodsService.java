package spharos.nu.goods.domain.goods.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;
import spharos.nu.goods.global.exception.CustomException;
import spharos.nu.goods.global.exception.errorcode.ErrorCode;

@RequiredArgsConstructor
@Service
public class GoodsService {

	private final GoodsRepository goodsRepository;

	public List<Goods> getAllGoods() {
		return goodsRepository.findAll();
	}
}
