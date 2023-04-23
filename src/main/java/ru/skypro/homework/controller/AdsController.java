package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAdsDTO;
import ru.skypro.homework.dto.FullAdsDTO;
import ru.skypro.homework.dto.ResponseWrapperAdsDTO;
import ru.skypro.homework.service.AdsService;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdsController {
    private final AdsService adsService;

    @Autowired
    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @Operation(summary = "Получить все объявления")
    @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseWrapperAdsDTO.class))})
    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @Operation(summary = "Получить информацию об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = FullAdsDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getAdById(id));
    }

    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PreAuthorize("@adsServiceImpl.getAdById(#id).email == authentication.name or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdById(@PathVariable Integer id) {
        adsService.deleteAdById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(@RequestPart("image") MultipartFile image, @RequestPart("properties") CreateAdsDTO properties, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.addAd(image, properties, authentication));
    }

    @Operation(summary = "Обновить информацию об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdsDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PreAuthorize("@adsServiceImpl.getAdById(#id).email == authentication.name or hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Integer id, @RequestBody CreateAdsDTO properties) {
        return ResponseEntity.ok(adsService.updateAd(id, properties));
    }

    @Operation(summary = "Получить объявления авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseWrapperAdsDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping("/me")
    public ResponseEntity<?> getMyAds(Authentication authentication) {
        return ResponseEntity.ok(adsService.getMyAds(authentication));
    }

    @Operation(summary = "Обновить картинку объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PreAuthorize("@adsServiceImpl.getAdById(#id).email == authentication.name or hasAuthority('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdImage(@PathVariable Integer id, @RequestParam MultipartFile image) {
        adsService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Показать картинку объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] showImageOnId(@PathVariable("id") Integer id) {
        return adsService.showImageOnId(id);
    }

    @Operation(summary = "Получить объявления авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseWrapperAdsDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping(value = "/search")
    public ResponseEntity<?> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(adsService.searchByTitle(title));
    }
}
