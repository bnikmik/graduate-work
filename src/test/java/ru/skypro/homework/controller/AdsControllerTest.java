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
import org.springframework.mock.web.MockPart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.dto.adsDTO.AdsDTO;
import ru.skypro.homework.dto.adsDTO.CreateAdsDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CustomerRepository;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdsControllerTest {
    private final MockPart mockImg = new MockPart("image", "image", "image".getBytes());
    private final MockPart mockImg2 = new MockPart("image", "image2", "image2".getBytes());
    private final CreateAdsDTO createAds = new CreateAdsDTO();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private Ad ad;
    private AdsDTO adsDTO;

    @BeforeEach
    void setUp() throws Exception {
        customer = new Customer();
        customer.setId(1);
        customer.setUsername("test@test.com");
        customer.setFirstName("testFirst");
        customer.setLastName("testLast");
        customer.setEnabled(true);
        customer.setPassword(encoder.encode("1234qwer"));
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

        adsDTO = new AdsDTO();
        adsDTO.setAuthor(customer.getId());
        adsDTO.setImage("/ads/image/" + ad.getId());
        adsDTO.setPk(ad.getId());
        adsDTO.setPrice(ad.getPrice());
        adsDTO.setTitle(ad.getTitle());

        createAds.setPrice(100);
        createAds.setTitle("title");
        createAds.setDescription("description");
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        adRepository.deleteAll();
    }

    @Test
    void testGetAllAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].title").value(ad.getTitle()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testAddAd() throws Exception {
        MockPart created = new MockPart("properties", objectMapper.writeValueAsBytes(createAds));
        mockMvc.perform(multipart("/ads")
                        .part(mockImg)
                        .part(created))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(adsDTO.getAuthor()))
                .andExpect(jsonPath("$.title").value(adsDTO.getTitle()))
                .andExpect(jsonPath("$.price").value(adsDTO.getPrice()));
    }


    @Test
    @WithMockUser(username = "test@test.com")
    void testGetAdById() throws Exception {
        mockMvc.perform(get("/ads/" + ad.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ad.getId()))
                .andExpect(jsonPath("$.authorFirstName").value(customer.getFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(customer.getLastName()))
                .andExpect(jsonPath("$.description").value(ad.getDescription()))
                .andExpect(jsonPath("$.email").value(customer.getUsername()))
                .andExpect(jsonPath("$.image").value(adsDTO.getImage()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()))
                .andExpect(jsonPath("$.price").value(ad.getPrice()))
                .andExpect(jsonPath("$.title").value(adsDTO.getTitle()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testDeleteAdById() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testUpdateAd() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", ad.getDescription());
        jsonObject.put("price", ad.getPrice());
        jsonObject.put("title", ad.getTitle());

        mockMvc.perform(patch("/ads/" + ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(adsDTO.getAuthor()))
                .andExpect(jsonPath("$.title").value(adsDTO.getTitle()))
                .andExpect(jsonPath("$.price").value(adsDTO.getPrice()));

    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testGetMyAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].title").value(ad.getTitle()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testUpdateAdImage() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getId() + "/image")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(request -> {
                    request.addPart(mockImg2);
                    return request;
                })).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void showImageOnId() throws Exception {
        mockMvc.perform(get("/ads/image/" + ad.getId()))
                .andExpect(status().isOk())
                .andExpect(content().bytes(ad.getImage()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void searchByTitle() throws Exception {
        mockMvc.perform(get("/ads/search?title=" + ad.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].title").value(ad.getTitle()));
    }
}