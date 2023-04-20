package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseWrapperCommentDTO {
    private Integer count;
    private List<CommentDTO> results;

    public static ResponseWrapperCommentDTO fromModel(List<CommentDTO> list) {
        ResponseWrapperCommentDTO dto = new ResponseWrapperCommentDTO();
        dto.setCount(list.size());
        dto.setResults(list);
        return dto;
    }
}
