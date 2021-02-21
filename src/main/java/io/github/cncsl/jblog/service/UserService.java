package io.github.cncsl.jblog.service;

import io.github.cncsl.jblog.domain.Authority;
import io.github.cncsl.jblog.domain.User;
import io.github.cncsl.jblog.exception.EmailAlreadyUsedException;
import io.github.cncsl.jblog.exception.UsernameAlreadyUsedException;
import io.github.cncsl.jblog.repository.AuthorityRepository;
import io.github.cncsl.jblog.repository.UserRepository;
import io.github.cncsl.jblog.security.AuthoritiesConstants;
import io.github.cncsl.jblog.security.SecurityUtils;
import io.github.cncsl.jblog.util.RandomUtil;
import io.github.cncsl.jblog.web.vm.RegisterUserVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 用户服务，注册、激活等业务逻辑
 *
 * @author cncsl
 */
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private AuthorityRepository authorityRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 新账号注册
     *
     * @param registerUserVM 注册账号
     * @return 注册的账号对象
     */
    public User registerUser(RegisterUserVM registerUserVM) {
        userRepository.findOneByUsername(registerUserVM.getUsername().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNotActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(registerUserVM.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNotActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(registerUserVM.getPassword());
        newUser.setUsername(registerUserVM.getUsername().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setDisplayName(registerUserVM.getDisplayName());
        newUser.setEmail(registerUserVM.getEmail().toLowerCase());
        // 新账号未激活状态
        newUser.setActivated(false);
        // 激活校验码
        newUser.setActivationKey(RandomUtil.generateRandomAlphanumericString());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * 激活账号
     *
     * @param key 激活校验码
     * @return 激活后包装账号信息的 Optional
     */
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key).map(user -> {
            // 激活账号
            user.setActivated(true);
            user.setActivationKey(null);
            log.debug("Activated user: {}", user);
            userRepository.save(user);
            return user;
        });
    }

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUsernameOptional().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    private boolean removeNotActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

}
