package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.commentDTO.CommentDTO;
import ru.skypro.homework.dto.commentDTO.ResponseWrapperCommentDTO;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
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
        return CommentMapper.fromModelToRWCDto(commentRepository.findAllByAd_Id(id)
                .stream()
                .map(CommentMapper::fromModelToDto)
                .collect(Collectors.toList()));
    }

    @Override
    public CommentDTO addCommentToAdById(Integer id, CommentDTO commentDTO, Authentication authentication) {
        if (commentDTO.getText() == null || commentDTO.getText().isBlank()) throw new BadRequestException();
        Comment comment = CommentMapper.fromDtoToModel(commentDTO);
        comment.setCustomer(customerRepository.findByUsername(authentication.getName()).orElseThrow(NotFoundException::new));
        comment.setAd(adRepository.findById(id).orElseThrow(NotFoundException::new));
        commentRepository.save(comment);
        return CommentMapper.fromModelToDto(comment);
    }

    @Override
    @Transactional
    public void deleteCommentById(Integer adId, Integer commentId) {
        commentRepository.deleteByIdAndAdId(commentId, adId);
    }

    @Override
    public CommentDTO updateCommentById(Integer adId, Integer commentId, CommentDTO commentDTO) {
        if (commentDTO.getText() == null || commentDTO.getText().isBlank()) throw new BadRequestException();
        Comment comment = commentRepository.findByIdAndAdId(commentId, adId).orElseThrow(NotFoundException::new);
        comment.setText(commentDTO.getText());
        return CommentMapper.fromModelToDto(commentRepository.save(comment));
    }

    public Comment getById(Integer id) {
        return commentRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
