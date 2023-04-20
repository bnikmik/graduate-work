package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.model.Ad;

@Getter
@Setter
public class AdsDTO {
    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

    public static AdsDTO fromModel(Ad ad) {
        AdsDTO dto = new AdsDTO();
        dto.setAuthor(ad.getCustomer().getId());
        dto.setImage("/ads/image/" + ad.getId());
        dto.setPk(ad.getId());
        dto.setPrice(ad.getPrice());
        dto.setTitle(ad.getTitle());
        return dto;
    }
}
