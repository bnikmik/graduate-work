package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.dto.ResponseWrapperAdsDTO;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CustomerRepository;
import ru.skypro.homework.service.AdsService;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdsServiceImpl implements AdsService {
    private final AdRepository adRepository;
    private final CustomerRepository customerRepository;


    public AdsServiceImpl(AdRepository adRepository, CustomerRepository customerRepository) {
        this.adRepository = adRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseWrapperAdsDTO getAllAds() {
        return ResponseWrapperAdsDTO.fromModel(adRepository.findAll()
                .stream()
                .map(AdsDTO::fromModel)
                .collect(Collectors.toList()));
    }

    @Override
    public AdsDTO addAd(MultipartFile image, CreateAdsDTO properties, Authentication authentication) {
        Ad ad = properties.toModel();
        ad.setCustomer(customerRepository.findByUsername(authentication.getName())
                .orElseThrow(NotFoundException::new));
        try {
            ad.setImage(image.getBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return AdsDTO.fromModel(adRepository.save(ad));
    }

    @Override
    public FullAdsDTO getAdById(Integer id) {
        return FullAdsDTO.fromModel(adRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    @Override
    @Transactional
    public void deleteAdById(Integer id) {
        adRepository.deleteById(id);
    }

    @Override
    public AdsDTO updateAd(Integer id, CreateAdsDTO properties) {
        Ad ad = adRepository.findById(id).orElseThrow(NotFoundException::new);
        ad.setTitle(properties.getTitle());
        ad.setDescription(properties.getDescription());
        ad.setPrice(properties.getPrice());
        return AdsDTO.fromModel(adRepository.save(ad));
    }

    @Override
    public ResponseWrapperAdsDTO getMyAds(Authentication authentication) {
        ResponseWrapperAdsDTO wrapperAdsDTO = new ResponseWrapperAdsDTO();
        wrapperAdsDTO.setResults(adRepository.findAllByCustomer_Username(authentication.getName())
                .stream()
                .map(AdsDTO::fromModel)
                .collect(Collectors.toList()));
        wrapperAdsDTO.setCount(wrapperAdsDTO.getResults().size());
        return wrapperAdsDTO;
    }

    @Override
    public void updateAdImage(Integer id, MultipartFile image) {
        Ad ad = adRepository.findById(id).orElseThrow(NotFoundException::new);
        try {
            ad.setImage(image.getBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        adRepository.save(ad);
    }

    @Override
    public byte[] showImageOnId(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow(NotFoundException::new);
        return ad.getImage();
    }

    @Override
    public ResponseWrapperAdsDTO searchByTitle(String title) {
        ResponseWrapperAdsDTO wrapperAdsDTO = new ResponseWrapperAdsDTO();
        wrapperAdsDTO.setResults(adRepository.findAllByTitleContainsIgnoreCase(title)
                .stream()
                .map(AdsDTO::fromModel)
                .collect(Collectors.toList()));
        wrapperAdsDTO.setCount(wrapperAdsDTO.getResults().size());
        return wrapperAdsDTO;
    }

}