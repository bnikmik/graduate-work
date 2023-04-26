package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.CustomerRepository;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class CommentsControllerTest {
    private final MockPart mockImg = new MockPart("image", "image", "image".getBytes());
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private CustomerRepository customerRepository;
    private ObjectMapper objectMapper;
    @Autowired
    private CommentRepository commentRepository;
    private Ad ad;
    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        Customer customer = new Customer();
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
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        adRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testGetAllCommentsByAdId() throws Exception {
        mockMvc.perform(get("/ads/" + ad.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].text").value(comment.getText()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testAddCommentToAdById() throws Exception {
        mockMvc.perform(post("/ads/" + ad.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(commentDTO.getAuthor()))
                .andExpect(jsonPath("$.authorImage").value(commentDTO.getAuthorImage()))
                .andExpect(jsonPath("$.text").value(commentDTO.getText()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testDeleteCommentById() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testUpdateCommentById() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk());
    }
}