package spharos.nu.notification.domain.fcm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class NotificationListDto {
	private Long totalCount;
	private Integer nowPage;
	private Integer maxPage;

	@JsonProperty("isLast")
	@Getter(AccessLevel.NONE)
	private boolean isLast;

	private List<NotificationInfoDto> notificationList;

}
