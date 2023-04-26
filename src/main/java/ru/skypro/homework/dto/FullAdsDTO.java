package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.model.Ad;

@Getter
@Setter
public class FullAdsDTO {
    private Integer pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer price;
    private String title;

    public static FullAdsDTO fromModel(Ad ad) {
        FullAdsDTO dto = new FullAdsDTO();
        dto.setPk(ad.getId());
        dto.setAuthorFirstName(ad.getCustomer().getFirstName());
        dto.setAuthorLastName(ad.getCustomer().getLastName());
        dto.setDescription(ad.getDescription());
        dto.setEmail(ad.getCustomer().getUsername());
        dto.setImage("/ads/image/" + ad.getId());
        dto.setPhone(ad.getCustomer().getPhone());
        dto.setPrice(ad.getPrice());
        dto.setTitle(ad.getTitle());
        return dto;
    }
}
