package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperCommentDTO;
import ru.skypro.homework.exception.BadParamException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.CustomerRepository;
import ru.skypro.homework.service.CommentsService;

import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl implements CommentsService {
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CommentsServiceImpl(CommentRepository commentRepository, AdRepository adRepository, CustomerRepository customerRepository) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseWrapperCommentDTO getAllCommentsByAdId(Integer id) {
        return ResponseWrapperCommentDTO.fromModel(commentRepository.findAllByAd_Id(id)
                .stream()
                .map(CommentDTO::fromModel)
                .collect(Collectors.toList()));
    }

    @Override
    public CommentDTO addCommentToAdById(Integer id, CommentDTO commentDTO, Authentication authentication) {
        if (commentDTO.getText() == null || commentDTO.getText().isBlank()) throw new BadParamException();
        Comment comment = commentDTO.toModel();
        comment.setCustomer(customerRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new));
        comment.setAd(adRepository.findById(id).orElseThrow(NotFoundException::new));
        commentRepository.save(comment);
        return CommentDTO.fromModel(comment);
    }

    @Override
    @Transactional
    public void deleteCommentById(Integer adId, Integer commentId) {
        commentRepository.deleteByIdAndAdId(commentId, adId);
    }

    @Override
    public CommentDTO updateCommentById(Integer adId, Integer commentId, CommentDTO commentDTO) {
        if (commentDTO.getText() == null || commentDTO.getText().isBlank()) throw new BadParamException();
        Comment comment = commentRepository.findByIdAndAdId(commentId, adId).orElseThrow(NotFoundException::new);
        comment.setText(commentDTO.getText());
        return CommentDTO.fromModel(commentRepository.save(comment));
    }

    public Comment getById(Integer id) {
        return commentRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
