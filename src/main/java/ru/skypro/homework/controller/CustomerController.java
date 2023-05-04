package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.customerDTO.CustomerDTO;
import ru.skypro.homework.dto.customerDTO.NewPasswordDTO;
import ru.skypro.homework.service.CustomersService;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Пользователи")
public class CustomerController {
    private final CustomersService customersService;

    @Autowired
    public CustomerController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @Operation(summary = "Обновление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PostMapping("/set_password")
    private ResponseEntity<?> setPassword(@RequestBody NewPasswordDTO newPasswordDTO, Authentication authentication) {
        customersService.setPassword(newPasswordDTO, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить информацию об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping("/me")
    private ResponseEntity<?> getMyInfo(Authentication authentication) {
        return ResponseEntity.ok(customersService.getMyInfo(authentication));
    }

    @Operation(summary = "Обновить информацию об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PatchMapping("/me")
    private ResponseEntity<?> updateMyInfo(@RequestBody CustomerDTO customerDTO, Authentication authentication) {
        return ResponseEntity.ok(customersService.updateMyInfo(customerDTO, authentication));
    }

    @Operation(summary = "Обновить аватар авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> updateMyAvatar(@RequestPart("image") MultipartFile avatar, Authentication authentication) {
        customersService.updateMyImage(avatar, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Показать аватар авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping(value = "/me/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] showAvatarOnId(@PathVariable("id") Integer id) {
        return customersService.showAvatarOnId(id);
    }
}


