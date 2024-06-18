package spharos.nu.etc.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReviewOneResponseDto {

	@Schema(description = "후기 점수 레벨")
	private Integer level;

	@Schema(description = "작성자")
	private String writerUuid;

	@Schema(description = "굿즈코드")
	private String goodsCode;

	@Schema(description = "후기 내용")
	private String content;
}
