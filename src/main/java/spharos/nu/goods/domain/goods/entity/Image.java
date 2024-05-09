package spharos.nu.goods.domain.goods.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long id;
	@NotBlank
	private String code;
	@NotBlank
	private String url;
	@NotNull
	private Integer index;
}
