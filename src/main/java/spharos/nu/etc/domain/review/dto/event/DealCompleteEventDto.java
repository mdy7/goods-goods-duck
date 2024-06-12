package spharos.nu.etc.domain.review.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DealCompleteEventDto {

	@Schema(description = "굿즈코드")
	private String goodsCode;
}
