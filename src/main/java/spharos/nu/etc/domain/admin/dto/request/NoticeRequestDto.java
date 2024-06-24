package spharos.nu.etc.domain.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequestDto {

	@Schema(description = "제목")
	private String title;

	@Schema(description = "내용")
	private String content;
}
