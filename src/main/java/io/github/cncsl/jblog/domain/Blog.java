package io.github.cncsl.jblog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 博客表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "blog_name", length = 20, nullable = false)
    private String name;

    @Size(max = 100)
    @Column(name = "desc_info", length = 100)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = "blogs", allowSetters = true)
    private User user;

}
