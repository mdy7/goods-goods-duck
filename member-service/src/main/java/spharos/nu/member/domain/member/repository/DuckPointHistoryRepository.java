package spharos.nu.member.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spharos.nu.member.domain.member.entity.DuckPointHistory;


@Repository
public interface DuckPointHistoryRepository extends JpaRepository<DuckPointHistory, Long> {

}
