package ru.skypro.homework.dto.adsDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseWrapperAdsDTO {
    private Integer count;
    private List<AdsDTO> results;
}
