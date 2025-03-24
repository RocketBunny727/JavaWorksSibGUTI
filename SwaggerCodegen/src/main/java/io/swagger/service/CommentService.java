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
        if (comment.getAuthor() != null) {
            userRepository.findByName(comment.getAuthor().getName())
                    .ifPresentOrElse(
                            comment::setAuthor,
                            () -> commentRepository.save(comment)
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
        commentRepository.findById(updatedComment.getId())
                .orElseThrow(() -> new RuntimeException("Comment with ID: " + updatedComment.getId() + " not found"));

        if (updatedComment.getAuthor() != null) {
            userRepository.findByName(updatedComment.getAuthor().getName())
                    .ifPresentOrElse(
                            updatedComment::setAuthor,
                            () -> commentRepository.save(updatedComment)
                    );
        }

        return commentRepository.save(updatedComment);
    }


    public void deleteComment(Long id) {

    }

    public Optional<User> findUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User addNewUser(User user) {
        return userRepository.save(user);
    }
}
