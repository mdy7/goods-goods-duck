package spharos.nu.member.domain.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import spharos.nu.member.domain.member.dto.LoginDto;
import spharos.nu.member.domain.member.entity.Member;
import spharos.nu.member.domain.member.repository.UserRepository;
import spharos.nu.member.global.exception.CustomException;
import spharos.nu.member.global.exception.errorcode.ErrorCode;
import spharos.nu.member.utils.jwt.JwtProvider;
import spharos.nu.member.utils.jwt.JwtToken;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public JwtToken login(LoginDto loginDto) {
		Member member = userRepository.findByUserId(loginDto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_ERROR);
		}

		return jwtProvider.createToken(member.getUuid());
	}
}
