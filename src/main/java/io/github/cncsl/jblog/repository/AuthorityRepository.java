package io.github.cncsl.jblog.repository;

import io.github.cncsl.jblog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
