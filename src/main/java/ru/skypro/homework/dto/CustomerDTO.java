package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.model.Customer;

@Getter
@Setter
public class CustomerDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String image;

    public static CustomerDTO fromModel(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getUsername());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhone(customer.getPhone());
        dto.setImage("/users/me/image/" + customer.getId());
        return dto;
    }
}
