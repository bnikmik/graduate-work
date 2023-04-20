package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperCommentDTO;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentsServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public ResponseWrapperCommentDTO getAllCommentsByAdId(Integer id) {
        return null;
    }

    @Override
    public CommentDTO addCommentToAdById(Integer id, CommentDTO commentDTO) {
        return null;
    }

    @Override
    public void deleteCommentById(Integer adId, Integer commentId) {

    }

    @Override
    public CommentDTO updateCommentById(Integer adId, Integer commentId, CommentDTO commentDTO) {
        return null;
    }
}
