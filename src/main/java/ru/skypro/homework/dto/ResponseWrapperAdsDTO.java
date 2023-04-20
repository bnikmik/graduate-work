package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.model.Ad;

import java.util.List;

@Getter
@Setter
public class ResponseWrapperAdsDTO {
    private Integer count;
    private List<AdsDTO> results;

    public static ResponseWrapperAdsDTO fromModel(List<AdsDTO> list) {
        ResponseWrapperAdsDTO dto = new ResponseWrapperAdsDTO();
        dto.setCount(list.size());
        dto.setResults(list);
        return dto;
    }
}
