package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.customerDTO.CustomUserDetails;
import ru.skypro.homework.dto.customerDTO.CustomerDTO;
import ru.skypro.homework.dto.customerDTO.RegisterReqDTO;
import ru.skypro.homework.model.Customer;

public class CustomerMapper {

    public static CustomerDTO fromModelToCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getUsername());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhone(customer.getPhone());
        dto.setImage("/users/me/image/" + customer.getId());
        return dto;
    }

    public static Customer fromRegDTOtoModel(RegisterReqDTO reg) {
        Customer customer = new Customer();
        customer.setUsername(reg.getUsername());
        customer.setPassword(reg.getPassword());
        customer.setFirstName(reg.getFirstName());
        customer.setLastName(reg.getLastName());
        customer.setPhone(reg.getPhone());
        return customer;
    }

    public static CustomUserDetails fromModelToCustomUserDetails(Customer customer) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setRole(customer.getRole());
        customUserDetails.setUsername(customer.getUsername());
        customUserDetails.setPassword(customer.getPassword());
        customUserDetails.setFirstName(customer.getFirstName());
        customUserDetails.setLastName(customer.getLastName());
        customUserDetails.setPhone(customer.getPhone());
        customUserDetails.setImage("/users/me/image/" + customer.getId());
        customUserDetails.setEnabled(true);
        return customUserDetails;
    }

    public static Customer fromCustomUserDetailsToModel(CustomUserDetails customUserDetails) {
        Customer customer = new Customer();
        customer.setId(customUserDetails.getId());
        customer.setUsername(customUserDetails.getUsername());
        customer.setFirstName(customUserDetails.getFirstName());
        customer.setLastName(customUserDetails.getLastName());
        customer.setPhone(customUserDetails.getPhone());
        customer.setPassword(customUserDetails.getPassword());
        customer.setEnabled(customUserDetails.isEnabled());
        customer.setRole(customUserDetails.getRole());
        return customer;
    }
}