package spharos.nu.member.domain.member.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllMemberResponseDto {

	@Schema(description = "총 회원 수")
	private Long totalCount;

	@Schema(description = "현재 페이지")
	private Integer nowPage;

	@Schema(description = "최대 페이지")
	private Integer maxPage;

	@Schema(description = "마지막 페이지 여부")
	private Boolean isLast;

	@Schema(description = "전체 회원 목록")
	private List<ProfileResponseDto> memberList;
}
