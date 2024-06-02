package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.member.domain.member.dto.DuckPointInfoDto;
import spharos.nu.member.domain.member.entity.DuckPointHistory;

@Repository
public interface PointHistoryRepository extends JpaRepository<DuckPointHistory, Long> {
	Optional<DuckPointHistory> findByUuid(String uuid);

	Page<DuckPointInfoDto> findByUuid(String uuid, Pageable pageable);
}
