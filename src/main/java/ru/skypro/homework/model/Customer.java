package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.skypro.homework.enums.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private Boolean enabled;
    private String firstName;
    private String lastName;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] avatar;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Ad> ads;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Comment> comments;
}
