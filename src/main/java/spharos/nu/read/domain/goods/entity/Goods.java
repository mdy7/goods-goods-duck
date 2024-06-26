package spharos.nu.read.domain.goods.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spharos.nu.read.domain.goods.dto.response.ImageDto;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "goods")
public class Goods {

	@Id
	private String id;
	private String goodsCode;
	private Long categoryId;
	private String sellerUuid;
	private String name;
	private Long minPrice;
	private String description;
	private LocalDateTime openedAt;
	private LocalDateTime closedAt;
	private byte wishTradeType;
	private byte tradingStatus;
	private Boolean isDisable;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<String> tagList;
	private List<ImageDto> imageList;
	private Long wishCount;
	private Long bidCount;
	private Long viewsCount;
}
