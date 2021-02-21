package io.github.cncsl.jblog.repository;

import io.github.cncsl.jblog.domain.Blog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;

    @Test
    void findWithUserByIdTest() {
        Blog blog = blogRepository.findById(1L).get();
        log.debug(blog.toString());
//        Blog blog = blogRepository.findWithUserById(1L);
//        assertNotNull(blog.getUser());
//        log.debug(blog.getUser().getUsername());
    }

}