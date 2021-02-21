package io.github.cncsl.jblog.service;

import io.github.cncsl.jblog.domain.Blog;
import io.github.cncsl.jblog.domain.User;
import io.github.cncsl.jblog.repository.BlogRepository;
import io.github.cncsl.jblog.repository.UserRepository;
import io.github.cncsl.jblog.security.AuthoritiesConstants;
import io.github.cncsl.jblog.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 博客服务
 *
 * @author nullptr
 */
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    private UserRepository userRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }

    /**
     * 创建（开通）博客
     *
     * @param blog 新博客对象，其中 id 字段一定为 null（控制器层校验）
     * @return 创建成功的博客对象，其中 id 字段已经不为 null
     */
    @Transactional(rollbackFor = Exception.class)
    public Blog create(Blog blog) {
        //必然是已登陆用户，下方 orElseThrow 不会抛出异常
        User user = userRepository.findOneByUsername(SecurityUtils.getCurrentUsernameString()).orElseThrow();
        //一个用户只能开通一个博客
        if (blogRepository.findByUser(user).isEmpty()) {
            blog.setUser(user);
            return blogRepository.save(blog);
        } else {
            throw new AccessDeniedException("only one blog is allowed per user");
        }
    }


    /**
     * 修改博客信息
     *
     * @param blog 已存在的博客对象，其中 id 字段一定不为 null（控制器层校验）
     * @return 修改成功的博客对象
     * @throws AccessDeniedException 当权限不为 ROLE_ADMIN 的用户企图修改其他人的博客信息时抛出
     */
    @Transactional(rollbackFor = Exception.class)
    public Blog update(Blog blog) {
        //如果不具有 ADMIN 权限检查是否为拥有者
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            //必然是已存在的 blog 对象，下方不会抛出异常
            Blog persisted = blogRepository.findById(blog.getId()).orElseThrow();
            if (!persisted.getUser().getUsername().equals(SecurityUtils.getCurrentUsernameString())) {
                throw new AccessDeniedException("can't modify others' resources");
            }
        }
        return blogRepository.save(blog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        //如果不具有 ADMIN 权限检查是否为拥有者
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            //必然是已存在的 blog 对象，下方不会抛出异常
            Blog persisted = blogRepository.findById(id).orElseThrow();
            if (!persisted.getUser().getUsername().equals(SecurityUtils.getCurrentUsernameString())) {
                throw new AccessDeniedException("can't modify others' resources");
            }
        }
        blogRepository.deleteById(id);
    }
}
