package io.github.cncsl.jblog.web.rest;

import io.github.cncsl.jblog.domain.User;
import io.github.cncsl.jblog.exception.ClientException;
import io.github.cncsl.jblog.exception.EmailAlreadyUsedException;
import io.github.cncsl.jblog.exception.InvalidPasswordException;
import io.github.cncsl.jblog.exception.UsernameAlreadyUsedException;
import io.github.cncsl.jblog.service.MailService;
import io.github.cncsl.jblog.service.UserService;
import io.github.cncsl.jblog.service.dto.UserDTO;
import io.github.cncsl.jblog.web.vm.RegisterUserVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * 用户账号相关的控制器，登陆、退出相关的在 {@code io.github.cncsl.jblog.security.AccountResource} 中
 *
 * @author cncsl
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class UserResource {

    private final UserService userService;

    private final MailService mailService;

    @Autowired
    public UserResource(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : 注册账号
     *
     * @param registerUserVM 注册账号的 view model
     * @throws InvalidPasswordException     {@code 400 (Bad Request)} 密码无效
     * @throws EmailAlreadyUsedException    {@code 400 (Bad Request)} 邮箱已被使用
     * @throws UsernameAlreadyUsedException {@code 400 (Bad Request)} 用户名已被使用
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody RegisterUserVM registerUserVM) {
        User user = userService.registerUser(registerUserVM);
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : 激活已注册的账号
     *
     * @param key 激活校验码
     * @throws ClientException {@code 404 (Not Found)} 无法根据校验码找到相关账号
     */
    @GetMapping("/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (user.isEmpty()) {
            throw new ClientException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET /profile} : 查询当前用户的账号信息
     *
     */
    @GetMapping("/profiles")
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
                .map(UserDTO::new)
                .orElseThrow();
    }

}
