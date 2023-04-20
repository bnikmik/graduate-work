package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperCommentDTO;

public interface CommentsService {
    ResponseWrapperCommentDTO getAllCommentsByAdId(Integer id);
    CommentDTO addCommentToAdById(Integer id, CommentDTO commentDTO);
    void deleteCommentById(Integer adId, Integer commentId);
    CommentDTO updateCommentById(Integer adId, Integer commentId, CommentDTO commentDTO);
}
