package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ad;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad,Integer> {
    List<Ad> findAllByCustomer_Username(String username);
    List<Ad> findAllByTitleContainsIgnoreCase(String title);

}
