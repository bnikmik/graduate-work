package ru.skypro.homework.service;

import ru.skypro.homework.dto.customerDTO.RegisterReqDTO;

public interface AuthService {
    void login(String userName, String password);

    void register(RegisterReqDTO registerReqDTO);
}
