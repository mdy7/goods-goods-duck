package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.member.domain.member.entity.MemberScore;

@Repository
public interface ScoreRepository extends JpaRepository<MemberScore, Long> {

	Optional<MemberScore> findByUuid(String uuid);
}
