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
    public ResponseEntity<List<Comment>> getComments() {
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
    public ResponseEntity<Comment> postComment(@Valid @RequestBody CommentRequest body) {
        Comment comment = new Comment();
        comment.setContent(body.getContent());
        comment.setDate(body.getDate());

        User author = body.getAuthor();
        if (author == null || author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidAuthorException("Author information is invalid or missing.");
        }

        Optional<User> existingUser = commentService.findUserByName(author.getName());
        if (existingUser.isPresent()) {
            comment.setAuthor(existingUser.get());
        } else {
            User newUser = new User();
            newUser.setName(author.getName());
            commentService.addNewUser(newUser);
            comment.setAuthor(newUser);
        }

        Comment savedComment = commentService.createComment(comment);
        return ResponseEntity.status(200).body(savedComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> apiV1CommentsIdPut(@PathVariable("id") Long id, @Valid @RequestBody CommentRequest body) {
        return commentService.getComment(id).map(existingComment -> {
            existingComment.setContent(body.getContent());
            existingComment.setDate(body.getDate());

            User author = body.getAuthor();
            if (author != null) {
                if (author.getName() == null || author.getName().isEmpty()) {
                    throw new InvalidAuthorException("Author information is invalid or missing.");
                }

                Optional<User> existingUser = commentService.findUserByName(author.getName());
                if (existingUser.isPresent()) {
                    existingComment.setAuthor(existingUser.get());
                } else {
                    User newUser = new User();
                    newUser.setName(author.getName());
                    commentService.addNewUser(newUser);
                    existingComment.setAuthor(newUser);
                }
            }

            try {
                Comment updatedComment = commentService.updateComment(existingComment);
                return ResponseEntity.ok(updatedComment);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new Comment());
            }
        }).orElse(ResponseEntity.notFound().build());
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
