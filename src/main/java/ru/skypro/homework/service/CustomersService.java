package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.customerDTO.CustomerDTO;
import ru.skypro.homework.dto.customerDTO.NewPasswordDTO;

public interface CustomersService {
    void setPassword(NewPasswordDTO newPasswordDTO, Authentication authentication);

    CustomerDTO getMyInfo(Authentication authentication);

    CustomerDTO updateMyInfo(CustomerDTO customerDTO, Authentication authentication);

    void updateMyImage(MultipartFile avatar, Authentication authentication);

    byte[] showAvatarOnId(Integer id);
}
