package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.ResponseWrapperCommentDTO;
import ru.skypro.homework.service.CommentsService;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentsController {
    private final CommentsService commentsService;

    @Autowired
    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @Operation(summary = "Получить комментарии объявления")
    @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseWrapperCommentDTO.class))})
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getAllCommentsByAdId(@PathVariable Integer id) {
        return ResponseEntity.ok(commentsService.getAllCommentsByAdId(id));
    }


    @Operation(summary = "Добавить комментарий к объявлению")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PostMapping("{id}/comments")
    public ResponseEntity<?> addCommentToAdById(@PathVariable Integer id, @RequestBody CommentDTO commentDTO, Authentication authentication) {
        return ResponseEntity.ok(commentsService.addCommentToAdById(id, commentDTO, authentication));
    }

    @Operation(summary = "Удалить комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("@commentsServiceImpl.getById(#commentId).customer.username == authentication.name or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCommentById(@PathVariable Integer adId, @PathVariable Integer commentId) {
        commentsService.deleteCommentById(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление комментария к объявлению")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("@commentsServiceImpl.getById(#commentId).customer.username == authentication.name or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateCommentById(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentsService.updateCommentById(adId, commentId, commentDTO));
    }
}
