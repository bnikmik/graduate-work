package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.adsDTO.AdsDTO;
import ru.skypro.homework.dto.adsDTO.CreateAdsDTO;
import ru.skypro.homework.dto.adsDTO.FullAdsDTO;
import ru.skypro.homework.dto.adsDTO.ResponseWrapperAdsDTO;

public interface AdsService {

    ResponseWrapperAdsDTO getAllAds();

    AdsDTO addAd(MultipartFile image, CreateAdsDTO properties, Authentication authentication);

    FullAdsDTO getAdById(Integer id);

    void deleteAdById(Integer id);

    AdsDTO updateAd(Integer id, CreateAdsDTO properties);

    ResponseWrapperAdsDTO getMyAds(Authentication authentication);

    void updateAdImage(Integer id, MultipartFile image);

    byte[] showImageOnId(Integer id);

    ResponseWrapperAdsDTO searchByTitle(String title);
}
