package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.model.Ad;

@Getter
@Setter
public class CreateAdsDTO {
    private String description;
    private Integer price;
    private String title;

    public Ad toModel() {
        Ad ad = new Ad();
        ad.setDescription(description);
        ad.setPrice(price);
        ad.setTitle(title);
        return ad;
    }
}
