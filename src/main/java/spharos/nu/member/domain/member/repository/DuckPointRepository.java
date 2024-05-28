package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.member.domain.member.entity.DuckPoint;

@Repository
public interface DuckPointRepository extends JpaRepository<DuckPoint, Long> {

	Optional<DuckPoint> findByUuid(String uuid);
}
