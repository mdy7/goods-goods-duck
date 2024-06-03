package spharos.nu.auth.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.auth.domain.auth.entity.Member;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUserId(String userId);
	Optional<Member> findByUuid(String uuid);
	Optional<Member> findByNickname(String nickname);
	Optional<Member> findByPhoneNumber(String phoneNumber);
}
