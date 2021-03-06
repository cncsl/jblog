package io.github.cncsl.jblog.web.vm;

import io.github.cncsl.jblog.service.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Size;

/**
 * 注册账号时使用的 view model
 *
 * @author cncsl
 */
@ToString
@NoArgsConstructor
public class RegisterUserVM extends UserDTO {

    @Getter
    @Size(min = 6, max = 20)
    private String password;

}
