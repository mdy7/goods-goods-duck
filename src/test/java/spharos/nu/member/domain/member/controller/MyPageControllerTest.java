package spharos.nu.member.domain.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import spharos.nu.member.domain.member.dto.ProfileResponseDto;
import spharos.nu.member.domain.member.service.MyPageService;
import spharos.nu.member.global.apiresponse.ApiResponse;
import spharos.nu.member.utils.jwt.JwtProvider;

@WebMvcTest(MyPageController.class)
class MyPageControllerTest {

	// json
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MyPageService myPageService;

	@MockBean
	private JwtProvider jwtProvider;

	@Test
	@DisplayName("회원 프로필 조회 Test")
	void getProfileTest() throws Exception {

		// given
		ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
			.profileImg("프로필_이미지_url")
			.nickname("스껄스껄")
			.favCategory("애니메이션")
			.build();

		String token = "테스트용_토큰";
		String uuid = "테스트용_uuid";

		Mockito.when(myPageService.profileGet(uuid)).thenReturn(profileResponseDto);
		Mockito.when(jwtProvider.getUuid(token)).thenReturn(uuid);

		// when & then
		mockMvc.perform(get("/v1/users")
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(ApiResponse.success(profileResponseDto, "프로필 조회 성공"))))
			// .andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.result.profileImg").value(profileResponseDto.getProfileImg()))
			.andExpect(jsonPath("$.result.nickname").value(profileResponseDto.getNickname()))
			.andExpect(jsonPath("$.result.favCategory").value(profileResponseDto.getFavCategory()))
			.andExpect(jsonPath("$.message").value("프로필 조회 성공"))
			.andDo(print());
	}
}