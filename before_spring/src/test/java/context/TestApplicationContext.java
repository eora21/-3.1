package context;

import com.mysql.cj.jdbc.Driver;
import info.Info;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import user.dao.UserDao;
import user.dao.UserDaoJdbc;
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
@ImportResource("classpath:testApplicationContext.xml")
public class TestApplicationContext {
    @Resource
    DataSource embeddedDatabase;

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
    public UserDao userDao() {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.setJdbcTemplate(dataSource());
        userDaoJdbc.setSqlService(sqlService());
        return userDaoJdbc;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("user.sqlservice.jaxb");
        return jaxb2Marshaller;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(embeddedDatabase);
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
        return new NormalLevelUpgradePolicy(userDao(), mailSender());
    }

    @Bean
    public UserService userService() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(userDao());
        userServiceImpl.setLevelUpgradePolicy(userLevelUpgradePolicy());
        return userServiceImpl;
    }

    @Bean
    public UserService testUserService() {
        TestUserService testUserService = new TestUserService();
        testUserService.setUserDao(userDao());
        testUserService.setLevelUpgradePolicy(userLevelUpgradePolicy());
        return testUserService;
    }
}
