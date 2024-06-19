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

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "goods")
public class Goods {

	@Id
	private String id;
	// @Field("goods_code")
	private String goodsCode;
	// @Field("category_id")
	private Long categoryId;
	// @Field("seller_uuid")
	private String sellerUuid;
	// @Field("name")
	private String name;
	// @Field("min_price")
	private Long minPrice;
	// @Field("description")
	private String description;
	// @Field("opened_at")
	private LocalDateTime openedAt;
	// @Field("closed_at")
	private LocalDateTime closedAt;
	// @Field("wish_trade_type")
	private byte wishTradeType;
	// @Field("trading_status")
	private byte tradingStatus;
	// @Field("is_disable")
	private Boolean isDisable;
	// @Field("created_at")
	private LocalDateTime createdAt;
	// @Field("updated_at")
	private LocalDateTime updatedAt;
	// @Field("tag_list")
	private List<String> tagList;
	// @Field("wish_count")
	private Long wishCount;
	// @Field("bid_count")
	private Long bidCount;
	// @Field("views_count")
	private Long viewsCount;
}
