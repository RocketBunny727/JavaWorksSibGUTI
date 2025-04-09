package io.swagger;

import io.swagger.model.Comment;
import io.swagger.model.User;
import io.swagger.repository.ICommentRepository;
import io.swagger.repository.IUserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CommentRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("1234");

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IUserRepository userRepository;

    @BeforeAll
    static void setUpDB() { postgres.start(); }

    @AfterAll
    static void tearDownDB() { postgres.stop(); }

    private OffsetDateTime validDate;
    private User validUser;
    private Comment validComment1;
    private Comment validComment2;

    @BeforeEach
    void setUp() {
        this.userRepository.deleteAll();
        this.commentRepository.deleteAll();

        this.validDate = OffsetDateTime.now();
        this.validUser = new User();
        validUser.setName("Mike");
        validUser = userRepository.save(validUser);
        this.validComment1 = new Comment();
        validComment1.setContent("Comment#1");
        validComment1.setAuthor(validUser);
        validComment1.setDate(validDate);
        validComment1 = commentRepository.save(validComment1);
        this.validComment2 = new Comment();
        validComment2.setContent("Comment#2");
        validComment2.setAuthor(validUser);
        validComment2.setDate(validDate);
        validComment2 = commentRepository.save(validComment2);
    }

    @Test
    void saveUser() {
        Optional<User> user = userRepository.findById(validUser.getId());

        assertTrue(user.isPresent());
        assertEquals(validUser.getId(), user.get().getId());
    }

    @Test
    void getCommentById() {
        Optional<Comment> comment = commentRepository.findById(validComment1.getId());

        assertTrue(comment.isPresent());
        assertEquals(validComment1, comment.get());
    }


    @Test
    void getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        System.out.println(comments.toString());

        assertFalse(comments.isEmpty());
        assertEquals(2, comments.size());
        assertEquals(validComment1, comments.get(0));
        assertEquals(validComment2, comments.get(1));
    }
}
