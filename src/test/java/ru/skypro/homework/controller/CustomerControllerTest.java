package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.skypro.homework.dto.CustomerDTO;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.CustomerRepository;

import java.util.Collection;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;
    private ObjectMapper objectMapper;

    private CustomerDTO customerDTO;
    private UserDetails userDetails;
    private Authentication auth;
    private Customer customer;
    private final MockPart mockImg = new MockPart("image", "image", "image".getBytes());
    private final MockPart mockImg2 = new MockPart("image", "image2", "image2".getBytes());

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        customer = new Customer();
        customer.setId(1);
        customer.setUsername("test@test.com");
        customer.setFirstName("testFirst");
        customer.setLastName("testLast");
        customer.setPhone("+79999999999");
        customer.setAvatar(mockImg.getInputStream().readAllBytes());
        customerRepository.save(customer);

        customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setEmail(customer.getUsername());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setImage("/users/me/image/" + customer.getId());
        userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(Role.USER.name()));
            }

            @Override
            public String getPassword() {
                return "password";
            }

            @Override
            public String getUsername() {
                return customer.getUsername();
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
        jdbcUserDetailsManager.createUser(userDetails);
        auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

    }

    @AfterEach
    void tearDown() {
        jdbcUserDetailsManager.deleteUser(customer.getUsername());
        customerRepository.deleteAll();
    }

    @Test
    void setPassword() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currentPassword", "password");
        jsonObject.put("newPassword", "newPassword");
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()).with(authentication(auth)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getMyInfo() throws Exception {
        mockMvc.perform(get("/users/me").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customer.getUsername()));
    }

    @Test
    void updateMyInfo() throws Exception {
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO))
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customerDTO.getEmail()));
    }

    @Test
    void updateMyAvatar() throws Exception {
        mockMvc.perform(patch("/users/me/image")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(authentication(auth))
                .with(request -> {
                    request.addPart(mockImg2);
                    return request;
                })).andExpect(status().isOk());
    }

    @Test
    void showAvatarOnId() throws Exception {
        mockMvc.perform(get("/users/me/image/" + customer.getId()).with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(customer.getAvatar()));
    }


}