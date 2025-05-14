package io.swagger.api;

import io.swagger.model.Comment;
import io.swagger.model.CommentRequest;
import io.swagger.model.User;
import io.swagger.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentApiController {

    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> apiV1AllCommentsGet() {
        List<Comment> comments = commentService.getComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> apiV1CommentsIdGet(@PathVariable("id") Long id) {
        return commentService.getComment(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comment> apiV1CommentPost(@Valid @RequestBody CommentRequest body) {
        Comment comment = new Comment();
        comment.setContent(body.getContent());
        comment.setAuthor(body.getAuthor());
        comment.setDate(body.getDate());

        return ResponseEntity.ok(commentService.createComment(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> apiV1CommentsIdPut(@PathVariable("id") Long id, @Valid @RequestBody CommentRequest body) {
        Comment updatedComment = new Comment();
        updatedComment.setId(id);
        updatedComment.setContent(body.getContent());
        updatedComment.setAuthor(body.getAuthor());
        updatedComment.setDate(body.getDate());

        return ResponseEntity.ok(commentService.updateComment(updatedComment));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apiV1CommentsIdDelete(@PathVariable("id") Long id) {
        if (commentService.getComment(id).isPresent()) {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
