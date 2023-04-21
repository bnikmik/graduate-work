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
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdRepository adRepository;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private Authentication auth;
    private Ad ad;
    private AdsDTO adsDTO;

    private UserDetails userDetails;

    private final MockPart mockImg = new MockPart("image", "image", "image".getBytes());
    private final MockPart mockImg2 = new MockPart("image", "image2", "image2".getBytes());

    @BeforeEach
    void setUp() throws Exception {
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

        adsDTO = new AdsDTO();
        adsDTO.setAuthor(customer.getId());
        adsDTO.setImage("/ads/image/" + ad.getId());
        adsDTO.setPk(ad.getId());
        adsDTO.setPrice(ad.getPrice());
        adsDTO.setTitle(ad.getTitle());

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
    }

    @Test
    void getAllAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].title").value(ad.getTitle()));
    }

    @Test
    void addAd() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", ad.getDescription());
        jsonObject.put("price", ad.getPrice());
        jsonObject.put("title", ad.getTitle());

        MockPart created = new MockPart("properties", jsonObject.toString().getBytes());
        mockMvc.perform(multipart("/ads")
                        .part(mockImg)
                        .part(created)
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(adsDTO.getAuthor()))
                .andExpect(jsonPath("$.title").value(adsDTO.getTitle()))
                .andExpect(jsonPath("$.price").value(adsDTO.getPrice()));
    }


    @Test
    void getAdById() throws Exception {
        mockMvc.perform(get("/ads/" + ad.getId()).with(authentication(auth)))
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
    void deleteAdById() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getId()).with(authentication(auth)))
                .andExpect(status().isOk());
    }

    @Test
    void updateAd() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", ad.getDescription());
        jsonObject.put("price", ad.getPrice());
        jsonObject.put("title", ad.getTitle());

        mockMvc.perform(patch("/ads/" + ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()).with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(adsDTO.getAuthor()))
                .andExpect(jsonPath("$.title").value(adsDTO.getTitle()))
                .andExpect(jsonPath("$.price").value(adsDTO.getPrice()));

    }

    @Test
    void getMyAds() throws Exception {
        mockMvc.perform(get("/ads").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    void updateAdImage() throws Exception {
        mockMvc.perform(patch("/ads/" + ad.getId() + "/image")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(authentication(auth))
                .with(request -> {
                    request.addPart(mockImg2);
                    return request;
                })).andExpect(status().isOk());
    }

    @Test
    void showImageOnId() throws Exception {
        mockMvc.perform(get("/ads/image/" + ad.getId()).with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(ad.getImage()));
    }

    @Test
    void searchByTitle() throws Exception {
        mockMvc.perform(get("/ads/search?title=" + ad.getTitle()).with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].title").value(ad.getTitle()));
    }
}