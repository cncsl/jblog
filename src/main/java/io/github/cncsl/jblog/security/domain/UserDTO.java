package io.github.cncsl.jblog.security.domain;

import io.github.cncsl.jblog.domain.Authority;
import io.github.cncsl.jblog.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @NotBlank
    @Size(max = 50)
    private String displayName;

    @NotBlank
    @Email
    @Size(min = 5, max = 256)
    private String email;

    private Set<String> authorities;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.authorities = user.getAuthorities().stream()
                .map(Authority::getName)
                .collect(Collectors.toSet());
    }

}
