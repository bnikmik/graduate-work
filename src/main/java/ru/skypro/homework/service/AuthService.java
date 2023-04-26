package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReqDTO;

public interface AuthService {
    void login(String userName, String password);

    void register(RegisterReqDTO registerReqDTO);
}
