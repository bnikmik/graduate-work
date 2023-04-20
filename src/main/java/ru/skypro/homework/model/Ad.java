package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ads")
@Getter
@Setter
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer price;
    private String title;
    private String description;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "ad")
    private List<Comment> comments;
}
