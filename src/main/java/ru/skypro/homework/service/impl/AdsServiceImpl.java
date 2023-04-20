package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.dto.ResponseWrapperAdsDTO;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CustomerRepository;
import ru.skypro.homework.service.AdsService;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {
    private final AdRepository adRepository;
    private final CustomerRepository customerRepository;


    public AdsServiceImpl(AdRepository adRepository, CustomerRepository customerRepository) {
        this.adRepository = adRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseWrapperAdsDTO getAllAds() {
        return null;
    }

    @Override
    public AdsDTO addAd(MultipartFile image, CreateAdsDTO properties, Authentication authentication) throws IOException {
        Ad ad = properties.toModel();
        ad.setCustomer(customerRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new));
        ad.setImage(image.getBytes());
        return AdsDTO.fromModel(adRepository.save(ad));

//        Ad ad = properties.toModel();
//        ad.setCustomer(customerRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new));
//        try {
//            byte[] bytes = image.getBytes();
//            ad.setImage(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        adRepository.saveAndFlush(ad);
//        return AdsDTO.fromModel(ad);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Override
    public FullAdsDTO getAdById(Integer id) {
        return null;
    }

    @Override
    public void deleteAdById(Integer id) {

    }

    @Override
    public AdsDTO updateAd(Integer id, CreateAdsDTO properties) {
        return null;
    }

    @Override
    public ResponseWrapperAdsDTO getMyAds(Authentication authentication) {
        List<AdsDTO> list = adRepository.findAll().stream().map(AdsDTO::fromModel).collect(Collectors.toList());
        ResponseWrapperAdsDTO wrapperAdsDTO = new ResponseWrapperAdsDTO();
        wrapperAdsDTO.setCount(list.size());
        wrapperAdsDTO.setResults(list);
        return wrapperAdsDTO;
    }

    @Override
    public void updateAdImage(Integer id, MultipartFile image) {

    }

    @Override
    public byte[] showImageOnId(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(NotFoundException::new);
        return ad.getImage();
    }

}
