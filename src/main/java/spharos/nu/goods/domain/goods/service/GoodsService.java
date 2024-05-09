package spharos.nu.goods.domain.goods.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;

@RequiredArgsConstructor
@Service
public class GoodsService {

	private final GoodsRepository goodsRepository;

	public List<Goods> getAllGoods() {
		return goodsRepository.findAll();
	}
}
