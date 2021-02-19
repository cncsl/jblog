package io.github.cncsl.jblog.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.cncsl.jblog.domain.User;
import io.github.cncsl.jblog.exception.ClientException;
import io.github.cncsl.jblog.exception.EmailAlreadyUsedException;
import io.github.cncsl.jblog.exception.InvalidPasswordException;
import io.github.cncsl.jblog.exception.UsernameAlreadyUsedException;
import io.github.cncsl.jblog.security.domain.LoginVM;
import io.github.cncsl.jblog.security.domain.RegisterUserVM;
import io.github.cncsl.jblog.security.jwt.JwtFilter;
import io.github.cncsl.jblog.security.jwt.TokenProvider;
import io.github.cncsl.jblog.service.MailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for user's account
 *
 * @author cncsl
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountResource {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsServiceImpl userDetailsService;

    private final MailService mailService;

    public AccountResource(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                           UserDetailsServiceImpl userDetailsService, MailService mailService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
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
        User user = userDetailsService.registerUser(registerUserVM);
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
        Optional<User> user = userDetailsService.activateRegistration(key);
        if (user.isEmpty()) {
            throw new ClientException("No user was found for this activation key");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> authorize(@Valid @RequestBody LoginVM vm) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(vm.getUsername(), vm.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //避免前端未填写 rememberMe 字段时由自动拆箱导致的空指针
        boolean rememberMe = vm.getRememberMe() != null && vm.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        //head
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.ACCEPTED);
    }

    @RequiredArgsConstructor
    private static class JwtToken {

        @Getter
        @JsonProperty("id_token")
        private final String idToken;

    }

}
