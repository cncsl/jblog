package io.github.cncsl.jblog.web.rest;

import io.github.cncsl.jblog.domain.Blog;
import io.github.cncsl.jblog.exception.BlogException;
import io.github.cncsl.jblog.exception.ClientException;
import io.github.cncsl.jblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.cncsl.jblog.domain.Blog}.
 *
 * @author cncsl
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class BlogResource {
    
    private final BlogService blogService;

    @Autowired
    public BlogResource(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/blogs")
    public ResponseEntity<Blog> createBlog(@Valid @RequestBody Blog blog) {
        log.debug("REST request to save Blog : {}", blog);
        if (blog.getId() != null) {
            throw new BlogException("A new blog cannot already have an ID");
        }
        Blog result = blogService.create(blog);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/blogs")
    public ResponseEntity<Blog> updateBlog(@Valid @RequestBody Blog blog) {
        log.debug("REST request to update Blog : {}", blog);
        if (blog.getId() == null) {
            throw new BlogException("A blog for modify must have an ID");
        }
        Blog result = blogService.update(blog);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable Long id) {
        log.debug("REST request to get Blog : {}", id);
        Optional<Blog> blog = blogService.findById(id);
        return new ResponseEntity<>(blog.orElseThrow(ClientException::new), HttpStatus.OK);
    }

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        log.debug("REST request to delete Blog : {}", id);
        blogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
