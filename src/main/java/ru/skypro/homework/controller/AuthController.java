package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginReqDTO;
import ru.skypro.homework.dto.RegisterReqDTO;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegisterReqDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDTO req) {
        authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Регистрация")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegisterReqDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReqDTO req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
