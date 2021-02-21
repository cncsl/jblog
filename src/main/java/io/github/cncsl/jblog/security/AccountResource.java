package io.github.cncsl.jblog.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.cncsl.jblog.security.jwt.JwtFilter;
import io.github.cncsl.jblog.security.jwt.TokenProvider;
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

    public AccountResource(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
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
