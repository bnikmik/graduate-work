package ru.skypro.homework.dto.adsDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdsDTO {
    private String description;
    private Integer price;
    private String title;
}
