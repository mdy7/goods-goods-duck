package spharos.nu.etc.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReviewListDto {

	@Schema(description = "거래id")
	private Long reviewId;

	@Schema(description = "굿즈 코드")
	private String goodsCode;

	@Schema(description = "후기 내용")
	private String content;
}
