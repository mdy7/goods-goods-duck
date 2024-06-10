package spharos.nu.etc.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReviewListDto {

	@Schema(description = "굿즈 코드")
	private String goodsCode;

	@Schema(description = "후기 내용")
	private String content;
}
