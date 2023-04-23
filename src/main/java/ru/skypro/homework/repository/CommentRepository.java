package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    void deleteByIdAndAdId(Integer adId, Integer commentId);

    List<Comment> findAllByAd_Id(Integer id);

    Optional<Comment> findByIdAndAdId(Integer adId, Integer commentId);
}
