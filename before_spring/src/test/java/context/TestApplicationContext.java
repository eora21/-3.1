package context;

import com.mysql.cj.jdbc.Driver;
import info.Info;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import user.dao.UserDao;
import user.domain.DummyMailSender;
import user.domain.NormalLevelUpgradePolicy;
import user.domain.UserLevelUpgradePolicy;
import user.service.UserService;
import user.service.UserServiceImpl;
import user.service.UserServiceTest.TestUserService;
import user.sqlservice.registry.EmbeddedDbSqlRegistry;
import user.sqlservice.registry.SqlRegistry;
import user.sqlservice.service.OxmSqlService;
import user.sqlservice.service.SqlService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "user")
public class TestApplicationContext {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;

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
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("user.sqlservice.jaxb");
        return jaxb2Marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .generateUniqueName(true)
                .build();
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(embeddedDatabase());
        return embeddedDbSqlRegistry;
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService oxmSqlService = new OxmSqlService();
        oxmSqlService.setUnmarshaller(unmarshaller());
        oxmSqlService.setSqlRegistry(sqlRegistry());
        return oxmSqlService;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        return new NormalLevelUpgradePolicy(userDao, mailSender());
    }

    @Bean
    public UserService testUserService() {
        return new TestUserService();
    }
}
