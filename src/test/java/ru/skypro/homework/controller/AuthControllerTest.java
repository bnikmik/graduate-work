package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.dto.RegisterReqDTO;
import ru.skypro.homework.enums.Role;

import java.util.Collection;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


    @BeforeEach
    void setUp() {
        reqDTO = new RegisterReqDTO();
        reqDTO.setPassword("password");
        reqDTO.setUsername("test1@test.com");
        reqDTO.setRole(Role.USER);
        userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(reqDTO.getRole().name()));
            }

            @Override
            public String getPassword() {
                return reqDTO.getPassword();
            }

            @Override
            public String getUsername() {
                return reqDTO.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

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
        jdbcUserDetailsManager.deleteUser(reqDTO.getUsername());
    }


    @Test
    void register_successfully() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "test@test.com");
        jsonObject.put("password", "pass");
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        userDetails = jdbcUserDetailsManager.loadUserByUsername("test@test.com");

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

        mockMvc.perform(get("/users/me").with(authentication(auth))).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));
        jdbcUserDetailsManager.deleteUser("test@test.com");
    }
}