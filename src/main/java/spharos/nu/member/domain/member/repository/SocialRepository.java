package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.member.domain.member.entity.SocialMember;

@Repository
public interface SocialRepository extends JpaRepository<SocialMember, Long> {
	Optional<SocialMember> findByMemberCode(String memberCode);
}
