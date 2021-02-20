package io.github.cncsl.jblog.web.rest;

import io.github.cncsl.jblog.domain.Blog;
import io.github.cncsl.jblog.exception.BlogException;
import io.github.cncsl.jblog.exception.ClientException;
import io.github.cncsl.jblog.repository.BlogRepository;
import io.github.cncsl.jblog.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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

    private final BlogRepository blogRepository;

    public BlogResource(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @PostMapping("/blogs")
    public ResponseEntity<Blog> createBlog(@Valid @RequestBody Blog blog) {
        log.debug("REST request to save Blog : {}", blog);
        if (blog.getId() != null) {
            throw new BlogException("A new blog cannot already have an ID");
        }
        Blog result = blogRepository.save(blog);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/blogs")
    public ResponseEntity<Blog> updateBlog(@Valid @RequestBody Blog blog) {
        log.debug("REST request to update Blog : {}", blog);
        if (blog.getId() == null) {
            throw new BlogException("A blog for modify must have an ID");
        }
        Blog result = blogRepository.save(blog);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/blogs")
    public List<Blog> getAllBlogs() {
        log.debug(SecurityUtils.getCurrentUsername());
        log.debug("REST request to get all Blogs");
        return blogRepository.findAll();
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable Long id) {
        log.debug("REST request to get Blog : {}", id);
        Optional<Blog> blog = blogRepository.findById(id);
        return new ResponseEntity<>(blog.orElseThrow(ClientException::new), HttpStatus.OK);
    }

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        log.debug("REST request to delete Blog : {}", id);
        blogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
