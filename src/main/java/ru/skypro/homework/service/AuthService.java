package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReqDTO;

public interface AuthService {
    boolean login(String userName, String password);
    void register(RegisterReqDTO registerReqDTO);
}
