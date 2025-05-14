package io.swagger.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "authorId", nullable = false)
  private User author;

  @Column(nullable = false)
  private OffsetDateTime date;
}
