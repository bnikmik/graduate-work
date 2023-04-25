package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.dto.RegisterReqDTO;
import ru.skypro.homework.enums.Role;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.enums.Role.USER;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserDetailsManager userDetailsManager;
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;
    private UserDetails userDetails;
    private RegisterReqDTO reqDTO;
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        reqDTO = new RegisterReqDTO();
        reqDTO.setFirstName("name");
        reqDTO.setLastName("name");
        reqDTO.setPhone("+79999999999");
        reqDTO.setPassword("password");
        reqDTO.setUsername("test@test.com");
        reqDTO.setRole(Role.USER);
        userDetails = User.builder()
                .passwordEncoder(this.encoder::encode)
                .password(reqDTO.getPassword())
                .username(reqDTO.getUsername())
                .roles(USER.name())
                .build();
    }

    @AfterEach
    void tearDown() {
        jdbcUserDetailsManager.deleteUser(reqDTO.getUsername());
    }

    @Test
    void login_successfully() throws Exception {
        jdbcUserDetailsManager.createUser(userDetails);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password", reqDTO.getPassword());
        jsonObject.put("username", reqDTO.getUsername());
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }


    @Test
    void register_successfully() throws Exception {
        reqDTO.setUsername("test1@test.com");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        userDetails = jdbcUserDetailsManager.loadUserByUsername(reqDTO.getUsername());

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

        mockMvc.perform(get("/users/me").with(authentication(auth))).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(reqDTO.getUsername()));
        jdbcUserDetailsManager.deleteUser(reqDTO.getUsername());
    }
}