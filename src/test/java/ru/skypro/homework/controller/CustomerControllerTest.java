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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.skypro.homework.dto.CustomerDTO;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.CustomerRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.enums.Role.USER;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class CustomerControllerTest {

    private final MockPart mockImg = new MockPart("image", "image", "image".getBytes());
    private final MockPart mockImg2 = new MockPart("image", "image2", "image2".getBytes());
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;
    private ObjectMapper objectMapper;
    private CustomerDTO customerDTO;
    @Autowired
    private PasswordEncoder encoder;
    private Customer customer;

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

        UserDetails userDetails = User.builder()
                .passwordEncoder(this.encoder::encode)
                .password("password")
                .username(customer.getUsername())
                .roles(USER.name())
                .build();
        jdbcUserDetailsManager.createUser(userDetails);
    }

    @AfterEach
    void tearDown() {
        jdbcUserDetailsManager.deleteUser(customer.getUsername());
        customerRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testSetPassword() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currentPassword", "password");
        jsonObject.put("newPassword", "newPassword");
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testGetMyInfo() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customer.getUsername()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testUpdateMyInfo() throws Exception {
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customerDTO.getEmail()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testUpdateMyAvatar() throws Exception {
        mockMvc.perform(patch("/users/me/image")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(request -> {
                    request.addPart(mockImg2);
                    return request;
                })).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testShowAvatarOnId() throws Exception {
        mockMvc.perform(get("/users/me/image/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().bytes(customer.getAvatar()));
    }
}