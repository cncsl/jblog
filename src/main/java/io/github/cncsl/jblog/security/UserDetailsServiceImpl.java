package io.github.cncsl.jblog.security;

import io.github.cncsl.jblog.domain.Authority;
import io.github.cncsl.jblog.domain.User;
import io.github.cncsl.jblog.exception.EmailAlreadyUsedException;
import io.github.cncsl.jblog.exception.UsernameAlreadyUsedException;
import io.github.cncsl.jblog.repository.AuthorityRepository;
import io.github.cncsl.jblog.repository.UserRepository;
import io.github.cncsl.jblog.security.domain.RegisterUserVM;
import io.github.cncsl.jblog.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户账号相关服务
 *
 * @author cncsl
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    /**
     * 根据用户名查找用户信息，封装为 {@code org.springframework.security.core.userdetails.User} 对象返回。
     *
     * @param username 用户名
     * @return Spring Security 框架使用的用户身份信息
     */
    @Override
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);
        Optional<User> userOptional = userRepository.findOneWithAuthoritiesByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException(username + "not found"));
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + username + " was not activated");
        }
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
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
            // activate given user for the registration key.
            user.setActivated(true);
            user.setActivationKey(null);
            log.debug("Activated user: {}", user);
            userRepository.save(user);
            return user;
        });
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
