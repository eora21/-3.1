package context;

import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import user.sqlservice.annotation.EnableSqlService;
import user.sqlservice.config.SqlMapConfig;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "user")
@EnableSqlService
@PropertySource("/config.properties")
public class AppContext implements SqlMapConfig {
    @Value("${db.driverClass}")
    Class<? extends Driver> driverClass;
    @Value("${db.url}")
    String url;
    @Value("${db.username}")
    String username;
    @Value("${db.password}")
    String password;
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    MailSender mailSender;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
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

    @Override
    public Resource getSqlMApResource() {
        return new ClassPathResource("sqlmap.xml");
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
