package io.github.cncsl.jblog.service;


import io.github.cncsl.jblog.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 邮箱服务
 */
@Slf4j
@Service
public class MailService {

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        log.debug("todo 实现发邮件逻辑");
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

}
