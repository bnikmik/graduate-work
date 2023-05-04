package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.adsDTO.AdsDTO;
import ru.skypro.homework.dto.adsDTO.CreateAdsDTO;
import ru.skypro.homework.dto.adsDTO.FullAdsDTO;
import ru.skypro.homework.dto.adsDTO.ResponseWrapperAdsDTO;
import ru.skypro.homework.model.Ad;

import java.util.List;

public class AdsMapper {

    public static AdsDTO fromModelToAdsDTO(Ad ad) {
        AdsDTO dto = new AdsDTO();
        dto.setAuthor(ad.getCustomer().getId());
        dto.setImage("/ads/image/" + ad.getId());
        dto.setPk(ad.getId());
        dto.setPrice(ad.getPrice());
        dto.setTitle(ad.getTitle());
        return dto;
    }

    public static FullAdsDTO fromModelToFullDTO(Ad ad) {
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

    public static Ad mapFromCreateAds(CreateAdsDTO dto) {
        Ad ad = new Ad();
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setTitle(dto.getTitle());
        return ad;
    }

    public static ResponseWrapperAdsDTO fromModelToRWADto(List<AdsDTO> list) {
        ResponseWrapperAdsDTO dto = new ResponseWrapperAdsDTO();
        dto.setCount(list.size());
        dto.setResults(list);
        return dto;
    }
}
