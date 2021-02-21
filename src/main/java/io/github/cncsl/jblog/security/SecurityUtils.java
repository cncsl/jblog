package io.github.cncsl.jblog.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * SpringSecurity Util
 *
 * @author cncsl
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前登陆账号的用户名，如果未登陆该方法会返回 null
     *
     * @return 当前登陆账号的用户名，如果未登陆返回 null
     */
    public static String getCurrentUsernameString() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return extractPrincipal(securityContext.getAuthentication());
    }

    /**
     * 获取当前登陆账号的用户名
     *
     * @return 当前登陆账号的用户名的 Optional 对象，如果未登陆该对象包装的值为 null
     */
    public static Optional<String> getCurrentUsernameOptional() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    /**
     * 检查当前用户是否具有指定权限
     *
     * @param authority 权限名
     * @return 返回 true 表示含有、false 表示不含有
     */
    public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                getAuthorities(authentication).anyMatch(authority::equals);
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority);
    }

}
