package spharos.nu.member.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileImageRequestDto {

	private String profileImage;

	public ProfileImageRequestDto(String profileImage) {
		this.profileImage = profileImage;
	}
}
