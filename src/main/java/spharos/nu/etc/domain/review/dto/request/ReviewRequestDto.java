package spharos.nu.etc.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

	@Schema(description = "낙찰자 uuid")
	private String bidderUuid;

	@Schema(description = "판매자 uuid")
	private String sellerUuid;

	@Schema(description = "굿즈코드")
	private String goodsCode;

	@Schema(description = "후기 점수")
	private Integer score;

	@Schema(description = "후기 내용")
	private String content;

	public ReviewRequestDto(String bidderUuid, String sellerUuid, String goodsCode, Integer score, String content) {
		this.bidderUuid = bidderUuid;
		this.sellerUuid = sellerUuid;
		this.goodsCode = goodsCode;
		this.score = score;
		this.content = content;
	}
}
