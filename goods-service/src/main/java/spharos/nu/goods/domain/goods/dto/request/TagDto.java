package spharos.nu.goods.domain.goods.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TagDto {
	@Schema(description = "태그index")
	private Long id;
	@Schema(description = "태그명")
	private String name;
}
