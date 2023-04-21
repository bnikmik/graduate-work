package ru.skypro.homework.controller;

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
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class CommentsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;
    private UserDetails userDetails;
    private Authentication auth;
    private Ad ad;
    private Customer customer;
    private Comment comment;

    private CommentDTO commentDTO;
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
        customer.setAds(new ArrayList<>() {{
            add(ad);
        }});
        customer.setAvatar(mockImg.getInputStream().readAllBytes());
        customerRepository.save(customer);

        ad = new Ad();
        ad.setId(1);
        ad.setTitle("title");
        ad.setPrice(100);
        ad.setDescription("desc");
        ad.setImage(mockImg.getInputStream().readAllBytes());
        ad.setCustomer(customer);
        adRepository.save(ad);

        comment = new Comment();
        comment.setId(1);
        comment.setText("test");
        comment.setAd(ad);
        comment.setCustomer(customer);
        commentRepository.save(comment);

        commentDTO = new CommentDTO();
        commentDTO.setAuthor(customer.getId());
        commentDTO.setAuthorImage("/users/me/image/" + customer.getId());
        commentDTO.setAuthorFirstName(customer.getFirstName());
        commentDTO.setCreatedAt(comment.getCreatedAt().toEpochMilli());
        commentDTO.setPk(comment.getId());
        commentDTO.setText(comment.getText());


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
        adRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void getAllCommentsByAdId() throws Exception {
        mockMvc.perform(get("/ads/" + ad.getId() + "/comments").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].text").value(comment.getText()));
    }

    @Test
    void addCommentToAdById() throws Exception {
        mockMvc.perform(post("/ads/" + ad.getId() + "/comments").with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(commentDTO.getAuthor()))
                .andExpect(jsonPath("$.authorImage").value(commentDTO.getAuthorImage()))
                .andExpect(jsonPath("$.text").value(commentDTO.getText()));

    }

    @Test
    void deleteCommentById() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getId() + "/comments/" + comment.getId()).with(authentication(auth)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCommentById() throws Exception{
        mockMvc.perform(patch("/ads/" + ad.getId() + "/comments/" + comment.getId()).with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk());
    }
}