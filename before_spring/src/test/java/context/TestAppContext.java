package context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import user.domain.DummyMailSender;
import user.service.UserService;
import user.service.UserServiceTest.TestUserService;

@Configuration
@Profile("test")
public class TestAppContext {
    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public UserService testUserService() {
        return new TestUserService();
    }
}
