package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.customerDTO.CustomUserDetails;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CustomerMapper;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.CustomerRepository;


@Service
public class CustomUserDetailsService implements UserDetailsManager {

    private final PasswordEncoder encoder;
    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(PasswordEncoder encoder, CustomerRepository customerRepository) {
        this.encoder = encoder;
        this.customerRepository = customerRepository;
    }

    @Override
    public void createUser(UserDetails user) {
        Customer saveUser = CustomerMapper.fromCustomUserDetailsToModel((CustomUserDetails) user);
        saveUser.setPassword(encoder.encode(user.getPassword()));
        customerRepository.save(saveUser);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return customerRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new BadRequestException();
        }
        return CustomerMapper.fromModelToCustomUserDetails(customerRepository.findByUsername(username).orElseThrow(NotFoundException::new));
    }
}
