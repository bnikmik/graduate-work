package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.commentDTO.CommentDTO;
import ru.skypro.homework.dto.commentDTO.ResponseWrapperCommentDTO;
import ru.skypro.homework.model.Comment;

import java.util.List;

public class CommentMapper {

    public static Comment fromDtoToModel(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        return comment;
    }

    public static CommentDTO fromModelToDto(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setAuthor(comment.getCustomer().getId());
        dto.setAuthorImage("/users/me/image/" + comment.getCustomer().getId());
        dto.setAuthorFirstName(comment.getCustomer().getFirstName());
        dto.setCreatedAt(comment.getCreatedAt().toEpochMilli());
        dto.setPk(comment.getId());
        dto.setText(comment.getText());
        return dto;
    }

    public static ResponseWrapperCommentDTO fromModelToRWCDto(List<CommentDTO> list) {
        ResponseWrapperCommentDTO dto = new ResponseWrapperCommentDTO();
        dto.setCount(list.size());
        dto.setResults(list);
        return dto;
    }
}
