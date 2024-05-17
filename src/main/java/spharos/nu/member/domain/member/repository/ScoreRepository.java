package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import spharos.nu.member.domain.member.entity.MemberScore;

@Repository
public interface ScoreRepository {

	Optional<MemberScore> findByUuid(String uuid);
}
