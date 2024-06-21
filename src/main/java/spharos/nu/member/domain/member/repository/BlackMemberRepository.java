package spharos.nu.member.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spharos.nu.member.domain.member.entity.BlackMember;

public interface BlackMemberRepository extends JpaRepository<BlackMember, Long> {
}
