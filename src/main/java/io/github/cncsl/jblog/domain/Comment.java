package io.github.cncsl.jblog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * 评论实体
 *
 * @author nullptr
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    private Comment relayTo;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @ManyToOne
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Post post;

}
