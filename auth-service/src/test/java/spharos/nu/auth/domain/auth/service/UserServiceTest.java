package spharos.nu.auth.domain.auth.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import spharos.nu.auth.domain.auth.dto.request.JoinDto;
import spharos.nu.auth.domain.auth.dto.request.LoginDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("로그인 성공")
    public void login() throws Exception {
        // given
        LoginDto loginDto = LoginDto.builder()
                .userId("test")
                .password("1234")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(loginDto);

        // when

        // then
        mockMvc.perform(post("/api/v1/auth-n/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
//                .andExpect(header().exists("Authorization"));
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 틀림")
    public void loginFail() throws Exception {
        // given
        LoginDto loginDto = LoginDto.builder()
                .userId("test")
                .password("12345")
                .build();

        // when

        // then
        mockMvc.perform(post("/api/v1/auth-n/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("로그인 실패 : 아이디 틀림")
    public void loginFail2() throws Exception {
        // given
        LoginDto loginDto = LoginDto.builder()
                .userId("test2")
                .password("1234")
                .build();

        // when

        // then
        mockMvc.perform(post("/api/v1/auth-n/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("로그인 실패 : 입력 값 없음")
    public void loginFail3() throws Exception {
        // given
        LoginDto loginDto = LoginDto.builder()
                .userId("")
                .password("")
                .build();

        // when

        // then
        mockMvc.perform(post("/api/v1/auth-n/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("회원가입 성공")
    public void join() throws Exception {
        JoinDto joinDto = JoinDto.builder()
                .userId("test2")
                .password("1234")
                .phoneNumber("010-1234-5678")
                .nickname("고라파덕")
                .profileImage("aaa.jpg")
                .favoriteCategory("ido")
                .build();


        mockMvc.perform(post("/api/v1/auth-n")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinDto)))
                .andExpect(status().isCreated());
    }
}