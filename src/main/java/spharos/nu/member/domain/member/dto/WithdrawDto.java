package spharos.nu.member.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import spharos.nu.member.domain.member.entity.Member;

@Getter
public class WithdrawDto {
	public Member withdrawMember(String uuid) {
		return Member.builder()
			.isWithdraw(true)
			.build();
	}
}
