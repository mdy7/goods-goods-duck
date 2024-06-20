package spharos.nu.goods.domain.goods.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ImageDto {
	@Schema(description = "이미지index")
	private Long id;
	@Schema(description = "이미지url")
	private String url;
}
