package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spharos.nu.member.domain.member.entity.Member;

public interface UserRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUserId(String userId);
}
