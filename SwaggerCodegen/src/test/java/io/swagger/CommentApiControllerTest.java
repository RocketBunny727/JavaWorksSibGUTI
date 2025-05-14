package io.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.CommentApiController;
import io.swagger.model.Comment;
import io.swagger.model.CommentRequest;
import io.swagger.model.User;
import io.swagger.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentApiController.class)
public class CommentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService service;

    @Autowired
    private ObjectMapper objectMapper;

    private OffsetDateTime date;
    private Comment comment;
    private User user;
    private CommentRequest commentRequest;

    @BeforeEach
    void setup() {
        date = OffsetDateTime.now();
        user = new User();
        user.setId(1L);
        user.setName("James");

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Working test!!!");
        comment.setAuthor(user);
        comment.setDate(date);

        commentRequest = new CommentRequest();
        commentRequest.setContent("Working test!!!");
        commentRequest.setDate(date);
        commentRequest.setAuthor(user);
    }

    @Test
    void shouldCreateComment() throws Exception {
        when(service.createComment(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Working test!!!"));
    }

    @Test
    void shouldGetComment() throws Exception {
        when(service.getComment(1L)).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/api/v1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Working test!!!"));
    }

    @Test
    void shouldUpdateComment() throws Exception {
        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setContent("New content");
        updatedComment.setAuthor(user);
        updatedComment.setDate(date);

        CommentRequest updatedRequest = new CommentRequest();
        updatedRequest.setContent("New content");
        updatedRequest.setDate(date);
        updatedRequest.setAuthor(user);

        when(service.getComment(1L)).thenReturn(Optional.of(comment));
        when(service.updateComment(any(Comment.class))).thenReturn(updatedComment);

        mockMvc.perform(put("/api/v1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("New content"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentComment() throws Exception {
        CommentRequest updatedRequest = new CommentRequest();
        updatedRequest.setContent("New content");
        updatedRequest.setDate(date);
        updatedRequest.setAuthor(user);

        when(service.getComment(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteComment() throws Exception {
        when(service.getComment(1L)).thenReturn(Optional.of(comment));
        doNothing().when(service).deleteComment(1L);

        mockMvc.perform(delete("/api/v1/comments/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteComment(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentComment() throws Exception {
        when(service.getComment(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/comments/1"))
                .andExpect(status().isNotFound());
    }
}