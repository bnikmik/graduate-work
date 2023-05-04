package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.customerDTO.RegisterReqDTO;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.ConflictException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.mapper.CustomerMapper;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    private final CustomUserDetailsService manager;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(CustomUserDetailsService manager, PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.encoder = passwordEncoder;
    }

    @Override
    public void login(String userName, String password) {
        if (!manager.userExists(userName)) throw new ForbiddenException();
        UserDetails userDetails = manager.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) throw new ForbiddenException();
    }

    @Override
    public void register(RegisterReqDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank() ||
                dto.getPassword() == null || dto.getPassword().isBlank() ||
                dto.getFirstName() == null || dto.getFirstName().isBlank() ||
                dto.getLastName() == null || dto.getLastName().isBlank() ||
                dto.getPhone() == null || dto.getPhone().isBlank()) throw new BadRequestException();
        if (manager.userExists(dto.getUsername())) {
            throw new ConflictException();
        }
        Customer customer = CustomerMapper.fromRegDTOtoModel(dto);
        customer.setRole(Role.USER);
        manager.createUser(CustomerMapper.fromModelToCustomUserDetails(customer));
    }
}
