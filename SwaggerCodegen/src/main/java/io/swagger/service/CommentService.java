package io.swagger.service;

import io.swagger.model.Comment;
import io.swagger.model.User;
import io.swagger.repository.ICommentRepository;
import io.swagger.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {

    private final ICommentRepository commentRepository;
    private final IUserRepository userRepository;

    public CommentService(ICommentRepository commentRepository, IUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Comment createComment(Comment comment) {

        log.info("createComment");

        if (comment.getAuthor() != null) {
            userRepository.findByName(comment.getAuthor().getName())
                    .ifPresentOrElse(
                            comment::setAuthor,
                            () -> comment.setAuthor(userRepository.save(comment.getAuthor()))
                    );
        }

        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    public Comment updateComment(Comment updatedComment) {

        log.info("updateComment");

        commentRepository.findById(updatedComment.getId())
                .orElseThrow(() -> new RuntimeException("Comment with ID: " + updatedComment.getId() + " not found"));

        if (updatedComment.getAuthor() != null) {
            userRepository.findByName(updatedComment.getAuthor().getName())
                    .ifPresentOrElse(
                            updatedComment::setAuthor,
                            () -> updatedComment.setAuthor(userRepository.save(updatedComment.getAuthor()))
                    );
        }

        return commentRepository.save(updatedComment);
    }

    public void deleteComment(Long id) {

        log.info("deleteComment");

        commentRepository.deleteById(id);
    }
}
