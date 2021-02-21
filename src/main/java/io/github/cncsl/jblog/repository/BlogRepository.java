package io.github.cncsl.jblog.repository;

import io.github.cncsl.jblog.domain.Blog;
import io.github.cncsl.jblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findByUser(User user);

}
