package spharos.nu.member.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Table(name = "member_info")
public class MemberInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_info_id", nullable = false)
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String uuid;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "favorite_category", nullable = false)
	private String favoriteCategory;

	@Column(name = "is_notify", nullable = false)
	private boolean isNotify;
}
