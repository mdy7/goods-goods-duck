package spharos.nu.auth.domain.auth.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spharos.nu.auth.domain.auth.entity.WithdrawMember;

@Repository
public interface WithdrawRepository extends JpaRepository<WithdrawMember, Long> {
	Optional<WithdrawMember> findByUuid(String uuid);
}
