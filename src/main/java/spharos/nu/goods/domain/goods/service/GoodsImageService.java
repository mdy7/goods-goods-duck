package spharos.nu.goods.domain.goods.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.goods.dto.event.GoodsCreateEventDto;
import spharos.nu.goods.domain.goods.dto.event.GoodsDeleteEventDto;
import spharos.nu.goods.domain.goods.dto.event.GoodsDisableEventDto;
import spharos.nu.goods.domain.goods.dto.event.GoodsStatusEventDto;
import spharos.nu.goods.domain.goods.dto.event.TradingCompleteEventDto;
import spharos.nu.goods.domain.goods.dto.request.GoodsCreateDto;
import spharos.nu.goods.domain.goods.dto.request.ImageDto;
import spharos.nu.goods.domain.goods.dto.request.TagDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsAllListDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsCodeDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsDetailDto;
import spharos.nu.goods.domain.goods.dto.response.GoodsSummaryDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.entity.Image;
import spharos.nu.goods.domain.goods.entity.Tag;
import spharos.nu.goods.domain.goods.kafka.GoodsKafkaProducer;
import spharos.nu.goods.domain.goods.repository.GoodsRepository;
import spharos.nu.goods.domain.goods.repository.ImageRepository;
import spharos.nu.goods.domain.goods.repository.TagRepository;
import spharos.nu.goods.global.exception.CustomException;
import spharos.nu.goods.global.exception.errorcode.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsImageService {

	private final ImageRepository imageRepository;

	public String getGoodsThumbnail(String goodsCode) {
		Image image = imageRepository.findByGoodsCodeAndIndex(goodsCode,0).orElseThrow();
		return image.getUrl();
	}

	public List<ImageDto> getGoodsImages(String goodsCode) {
		List<Image> images = imageRepository.findAllByGoodsCode(goodsCode);
		return images.stream().map(image -> ImageDto.builder().id(image.getIndex()).url(image.getUrl()).build()).toList();
	}
}
