package spharos.nu.goods.domain.goods.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.domain.goods.dto.request.ImageDto;
import spharos.nu.goods.domain.goods.dto.request.TagDto;
import spharos.nu.goods.domain.goods.entity.Goods;
import spharos.nu.goods.domain.goods.entity.Image;
import spharos.nu.goods.domain.goods.entity.Tag;
import spharos.nu.goods.domain.goods.repository.ImageRepository;
import spharos.nu.goods.domain.goods.repository.TagRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsTagService {

	private final TagRepository tagRepository;

	public List<TagDto> getGoodsTags(String goodsCode) {
		List<Tag> tags = tagRepository.findAllByGoodsCode(goodsCode);
		return tags.stream().map(tag -> TagDto.builder().id(tag.getId()).name(tag.getName()).build()).toList();
	}
}
