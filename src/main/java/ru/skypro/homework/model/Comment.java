package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    private Integer id;
    private Instant createdAt;
    private String text;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Ad ad;
}
