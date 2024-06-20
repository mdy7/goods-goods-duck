package spharos.nu.read.domain.goods.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SearchWordDto {

	@Schema(description = "검색어")
	private String keyword;
}
