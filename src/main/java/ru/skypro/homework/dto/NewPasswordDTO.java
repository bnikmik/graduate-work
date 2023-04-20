package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordDTO {
    private String currentPassword;
    private String newPassword;
}
