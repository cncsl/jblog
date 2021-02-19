package io.github.cncsl.jblog.security;


/**
 * 权限名常量
 *
 * @author cncsl
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String WRITER = "ROLE_WRITER";

    /**
     * 未登录用户使用匿名权限
     */
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
        throw new IllegalStateException("not allow");
    }
}
