package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CustomerDTO;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.service.CustomersService;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class CustomerController {
    private final CustomersService customersService;

    @Autowired
    public CustomerController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @PostMapping("/set_password")
    private ResponseEntity<?> setPassword(@RequestBody NewPasswordDTO newPasswordDTO, Authentication authentication) {
        customersService.setPassword(newPasswordDTO, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    private ResponseEntity<?> getMyInfo(Authentication authentication) {
        return ResponseEntity.ok(customersService.getMyInfo(authentication));
    }

    @PatchMapping("/me")
    private ResponseEntity<?> updateMyInfo(@RequestBody CustomerDTO customerDTO, Authentication authentication) {
        return ResponseEntity.ok(customersService.updateMyInfo(customerDTO, authentication));
    }

    @RequestMapping(value = "/me/image", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> updateMyAvatar(@RequestPart("image") MultipartFile avatar, Authentication authentication) {
        customersService.updateMyImage(avatar, authentication);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/me/image/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] showAvatarOnId(@PathVariable("id") Integer id) {
        return customersService.showAvatarOnId(id);
    }
}


