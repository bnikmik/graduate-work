package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.dto.ResponseWrapperAdsDTO;

import java.io.IOException;

public interface AdsService {

    ResponseWrapperAdsDTO getAllAds();

    AdsDTO addAd(MultipartFile image, CreateAdsDTO properties,  Authentication authentication) throws IOException;

    FullAdsDTO getAdById(Integer id);

    void deleteAdById(Integer id);

    AdsDTO updateAd(Integer id, CreateAdsDTO properties);

    ResponseWrapperAdsDTO getMyAds(Authentication authentication);

    void updateAdImage(Integer id, MultipartFile image);

    byte[] showImageOnId(Integer id);

}
