package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.model.Comment;

@Getter
@Setter
public class CommentDTO {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;

    public static CommentDTO fromModel(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setAuthor(comment.getCustomer().getId());
        dto.setAuthorImage("/users/me/image/" + comment.getCustomer().getId());
        dto.setAuthorFirstName(comment.getCustomer().getFirstName());
        dto.setCreatedAt(comment.getCreatedAt().toEpochMilli());
        dto.setPk(comment.getId());
        dto.setText(comment.getText());
        return dto;
    }

    public Comment toModel() {
        Comment comment = new Comment();
        comment.setText(text);
        return comment;
    }

}
