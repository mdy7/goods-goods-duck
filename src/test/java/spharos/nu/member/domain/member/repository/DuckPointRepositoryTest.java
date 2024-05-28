package spharos.nu.member.domain.member.repository;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import spharos.nu.member.domain.member.entity.DuckPoint;

@DataJpaTest
class DuckPointRepositoryTest {

	@Autowired
	private DuckPointRepository duckPointRepository;

	@Test
	@DisplayName("덕포인트 조회")
	void findByUuid() {

		// given
		DuckPoint duckPoint = DuckPoint.builder()
			.uuid("test_uuid1")
			.nowPoint(1000000L)
			.build();
		duckPointRepository.save(duckPoint);

		// when
		DuckPoint duckPoint1 = duckPointRepository.findByUuid("test_uuid1").orElseThrow();

		// then
		Assertions.assertThat(duckPoint1.getNowPoint()).isEqualTo(1000000L);

		// when, then non-existing UUID
		Assertions.assertThatThrownBy(() -> {
			DuckPoint duckPoint2 = duckPointRepository.findByUuid("test_uuid2").orElseThrow();
		}).isInstanceOf(NoSuchElementException.class);
	}
}