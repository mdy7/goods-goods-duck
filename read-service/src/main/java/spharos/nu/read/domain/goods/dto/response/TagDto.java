package spharos.nu.read.domain.goods.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TagDto {

	@Schema(description = "태그 인덱스")
	private Integer id;

	@Schema(description = "태그")
	private String name;
}
