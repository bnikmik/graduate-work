package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.CustomerDTO;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.Customer;
import ru.skypro.homework.repository.CustomerRepository;
import ru.skypro.homework.service.CustomersService;

import java.io.IOException;

@Service
@Slf4j
public class CustomersServiceImpl implements CustomersService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomersServiceImpl(UserDetailsManager manager, PasswordEncoder encoder, CustomerRepository customerRepository) {
        this.manager = manager;
        this.encoder = encoder;
        this.customerRepository = customerRepository;
    }

    @Override
    public void setPassword(NewPasswordDTO newPasswordDTO, Authentication authentication) {
        manager.changePassword(newPasswordDTO.getCurrentPassword(), encoder.encode(newPasswordDTO.getNewPassword()));
    }

    @Override
    public CustomerDTO getMyInfo(Authentication authentication) {
        return CustomerDTO.fromModel(customerRepository
                .findByUsername(authentication.getName())
                .orElseThrow(NotFoundException::new));
    }

    @Override
    public CustomerDTO updateMyInfo(CustomerDTO customerDTO, Authentication authentication) {
        Customer customer = customerRepository.findByUsername(authentication.getName())
                .orElseThrow(NotFoundException::new);
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhone(customerDTO.getPhone());
        customerRepository.save(customer);
        return customerDTO;
    }

    @Override
    public void updateMyImage(MultipartFile avatar, Authentication authentication) {
        Customer customer = customerRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new);
        try {
            customer.setAvatar(avatar.getBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        customerRepository.save(customer);
    }

    @Override
    public byte[] showAvatarOnId(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(NotFoundException::new)
                .getAvatar();
    }
}