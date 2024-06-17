package spharos.nu.auth.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class WithdrawDto {

	@Schema(description = "탈퇴사유")
	private String reason;
}