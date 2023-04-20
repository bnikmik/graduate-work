package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    void deleteByIdAndAdId(Integer adId, Integer commentId);

    Optional<Comment> findByIdAndAdId(Integer adId, Integer commentId);
}
