package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReqDTO;
import ru.skypro.homework.exception.ConflictException;
import ru.skypro.homework.repository.CustomerRepository;
import ru.skypro.homework.service.AuthService;

import static ru.skypro.homework.enums.Role.ADMIN;
import static ru.skypro.homework.enums.Role.USER;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserDetailsManager manager;
  private final CustomerRepository customerRepository;
  private final PasswordEncoder encoder;

  public AuthServiceImpl(UserDetailsManager manager, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
    this.manager = manager;
    this.customerRepository = customerRepository;
    this.encoder = passwordEncoder;
  }

  @Override
  public boolean login(String userName, String password) {
    if (!manager.userExists(userName)) {
      return false;
    }
    UserDetails userDetails = manager.loadUserByUsername(userName);
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public void register(RegisterReqDTO dto) {
    if (manager.userExists(dto.getUsername())) {
      throw new ConflictException();
    }
    manager.createUser(
        User.builder()
            .passwordEncoder(this.encoder::encode)
            .password(dto.getPassword())
            .username(dto.getUsername())
            .roles(ADMIN.name())
            .build());
    customerRepository.save(dto.toModel());
  }
}
