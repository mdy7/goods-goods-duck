package spharos.nu.member.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.member.domain.member.entity.MemberInfo;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

	Optional<MemberInfo> findByUuid(String uuid);

	Optional<MemberInfo> findByNickname(String nickname);
}
