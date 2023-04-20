package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.service.AdsService;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;

    @Autowired
    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(@RequestPart("image") MultipartFile image, @RequestPart CreateAdsDTO properties, Authentication authentication) {
        return ResponseEntity.ok(adsService.addAd(image, properties, authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getAdById(id));
    }

    @PreAuthorize("adsServiceImpl.getAdById(#id).email" + "== authentication.principal.username or (hasAuthority('ADMIN'))")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdById(@PathVariable Integer id) {
        adsService.deleteAdById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("adsServiceImpl.getAdById(#id).email" + "== authentication.principal.username or (hasAuthority('ADMIN'))")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Integer id, @RequestBody CreateAdsDTO properties) {
        return ResponseEntity.ok(adsService.updateAd(id, properties));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyAds(Authentication authentication) {
        return ResponseEntity.ok(adsService.getMyAds(authentication));
    }

    @PreAuthorize("adsServiceImpl.getAdById(#id).email" + "== authentication.principal.username or (hasAuthority('ADMIN'))")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdImage(@PathVariable Integer id, @RequestParam MultipartFile image) {
        adsService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] showImageOnId(@PathVariable("id") Integer id) {
        return adsService.showImageOnId(id);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(adsService.searchByTitle(title));
    }
}
