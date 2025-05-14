package io.swagger;

import io.swagger.model.Comment;
import io.swagger.model.User;
import io.swagger.repository.ICommentRepository;
import io.swagger.repository.IUserRepository;
import io.swagger.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private ICommentRepository commentRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private OffsetDateTime date;
    private Comment comment;
    private User user;

    @BeforeEach
    void setUp() {
        date = OffsetDateTime.now();
        user = new User(1L, "James");
        comment = new Comment(1L, "Working test!!!", user, date);
    }

    @Test
    void addNewComment_andShouldCreateNewUser() throws Exception {
        when(userRepository.findByName("James")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment newComment = commentService.createComment(comment);

        assertEquals(1L, newComment.getId());
        assertEquals("Working test!!!", newComment.getContent());
        assertEquals(user, newComment.getAuthor());
    }

    @Test
    void updateComment_andShouldUpdateUser() throws Exception {
        User newUser = new User(2L, "Max");
        Comment newComment = new Comment(1L, "Working updating test!!!", newUser, date);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(newComment));
        when(userRepository.findByName("Max")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);

        Comment updatedComment = commentService.updateComment(newComment);

        assertEquals(1L, updatedComment.getId());
        assertEquals("Working updating test!!!", updatedComment.getContent());
        assertEquals(newComment.getAuthor().getName(), updatedComment.getAuthor().getName());
    }

    @Test
    void shouldDeleteComment() throws Exception {
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }
}
