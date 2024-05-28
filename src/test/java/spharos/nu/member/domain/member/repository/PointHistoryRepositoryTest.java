package spharos.nu.member.domain.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import spharos.nu.member.domain.member.dto.DuckPointInfoDto;
import spharos.nu.member.domain.member.entity.DuckPointHistory;

@DataJpaTest
class PointHistoryRepositoryTest {

	@Autowired
	private PointHistoryRepository pointHistoryRepository;

	private String uuid;
	private Integer index;
	private Pageable pageable;

	@Test
	@DisplayName("덕포인트 상세내역")
	void findByUuid() {

		// given
		DuckPointHistory duckPointHistory = DuckPointHistory.builder()
			.uuid("test_uuid1")
			.leftPoint(50000L)
			.changeAmount(5000L)
			.changeStatus(false)
			.historyDetail("보증금")
			.build();
		pointHistoryRepository.save(duckPointHistory);

		uuid = "test_uuid1";
		index = 0;
		pageable = PageRequest.of(index, 10, Sort.by("createdAt").descending());

		// when
		Page<DuckPointInfoDto> result = pointHistoryRepository.findByUuid(uuid, pageable);

		// then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getContent()).isNotEmpty();
		Assertions.assertThat(result.getContent().get(0).getHistoryDetail()).isEqualTo("보증금");
	}
}