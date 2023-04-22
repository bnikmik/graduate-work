package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.service.CommentsService;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class CommentsController {
    private final CommentsService commentsService;

    @Autowired
    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getAllCommentsByAdId(@PathVariable Integer id) {
        return ResponseEntity.ok(commentsService.getAllCommentsByAdId(id));
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<?> addCommentToAdById(@PathVariable Integer id, @RequestBody CommentDTO commentDTO, Authentication authentication) {
        return ResponseEntity.ok(commentsService.addCommentToAdById(id, commentDTO, authentication));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("@commentsServiceImpl.getById(#commentId).customer.username == authentication.name or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCommentById(@PathVariable Integer adId, @PathVariable Integer commentId) {
        commentsService.deleteCommentById(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("@commentsServiceImpl.getById(#commentId).customer.username == authentication.name or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateCommentById(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentsService.updateCommentById(adId, commentId, commentDTO));
    }
}
