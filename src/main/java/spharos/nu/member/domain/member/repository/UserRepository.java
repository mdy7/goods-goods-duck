package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.member.domain.member.entity.Member;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUserId(String userId);
	Optional<Member> findByUuid(String uuid);
	Optional<Member> findByNick(String nickname);
	Optional<Member> findByPhone(String phoneNumber);
}
