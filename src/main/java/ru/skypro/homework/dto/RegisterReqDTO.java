package ru.skypro.homework.dto;

import lombok.Data;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.model.Customer;

@Data
public class RegisterReqDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

    public Customer toModel() {
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        return customer;
    }
}
