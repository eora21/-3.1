package context;

import com.mysql.cj.jdbc.Driver;
import info.Info;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import user.dao.UserDao;
import user.domain.NormalLevelUpgradePolicy;
import user.domain.UserLevelUpgradePolicy;
import user.service.UserService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "user")
@Import(SqlServiceContext.class)
public class AppContext {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    MailSender mailSender;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/toby_spring");
        dataSource.setUsername(Info.MYSQL_ID.getValue());
        dataSource.setPassword(Info.MYSQL_PASSWORD.getValue());
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(UserDao userDao, MailSender mailSender) {
        return new NormalLevelUpgradePolicy(userDao, mailSender);
    }

    @Configuration
    @Profile("production")
    public static class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("mail.mycompoany.com");
            return mailSender;
        }
    }

    @Configuration
    @Profile("local")
    public static class LocalAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            return mailSender;
        }
    }
}
