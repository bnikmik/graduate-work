package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.adsDTO.AdsDTO;
import ru.skypro.homework.dto.adsDTO.CreateAdsDTO;
import ru.skypro.homework.dto.adsDTO.FullAdsDTO;
import ru.skypro.homework.dto.adsDTO.ResponseWrapperAdsDTO;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CustomerRepository;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;
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
        return AdsMapper.fromModelToRWADto(adRepository.findAll()
                .stream()
                .map(AdsMapper::fromModelToAdsDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public AdsDTO addAd(MultipartFile image, CreateAdsDTO properties, Authentication authentication) {
        if (properties.getDescription() == null || properties.getDescription().isBlank() ||
                properties.getTitle() == null || properties.getTitle().isBlank())
            throw new BadRequestException();
        Ad ad = AdsMapper.mapFromCreateAds(properties);
        ad.setCustomer(customerRepository.findByUsername(authentication.getName())
                .orElseThrow(NotFoundException::new));
        try {
            ad.setImage(image.getBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return AdsMapper.fromModelToAdsDTO(adRepository.save(ad));
    }

    @Override
    public FullAdsDTO getAdById(Integer id) {
        return AdsMapper.fromModelToFullDTO(adRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    @Override
    @Transactional
    public void deleteAdById(Integer id) {
        adRepository.deleteById(id);
    }

    @Override
    public AdsDTO updateAd(Integer id, CreateAdsDTO properties) {
        if (properties.getDescription() == null || properties.getDescription().isBlank() ||
                properties.getTitle() == null || properties.getTitle().isBlank())
            throw new BadRequestException();
        Ad ad = adRepository.findById(id).orElseThrow(NotFoundException::new);
        ad.setTitle(properties.getTitle());
        ad.setDescription(properties.getDescription());
        ad.setPrice(properties.getPrice());
        return AdsMapper.fromModelToAdsDTO(adRepository.save(ad));
    }

    @Override
    public ResponseWrapperAdsDTO getMyAds(Authentication authentication) {
        ResponseWrapperAdsDTO wrapperAdsDTO = new ResponseWrapperAdsDTO();
        wrapperAdsDTO.setResults(adRepository.findAllByCustomer_Username(authentication.getName())
                .stream()
                .map(AdsMapper::fromModelToAdsDTO)
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
                .map(AdsMapper::fromModelToAdsDTO)
                .collect(Collectors.toList()));
        wrapperAdsDTO.setCount(wrapperAdsDTO.getResults().size());
        return wrapperAdsDTO;
    }

}
