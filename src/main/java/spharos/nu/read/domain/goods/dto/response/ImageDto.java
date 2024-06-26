package spharos.nu.read.domain.goods.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageDto {
	@Schema(description = "이미지index")
	private Integer id;
	@Schema(description = "이미지url")
	private String url;
}
