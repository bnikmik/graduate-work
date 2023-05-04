package ru.skypro.homework.dto.customerDTO;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.enums.Role;

@Getter
@Setter
public class RegisterReqDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
