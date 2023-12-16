package context;

import com.mysql.cj.jdbc.Driver;
import info.Info;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import user.dao.UserDao;
import user.dao.UserDaoJdbc;
import user.sqlservice.service.SqlService;

@Configuration
@ImportResource("classpath:testApplicationContext.xml")
public class TestApplicationContext {
    @Autowired
    SqlService sqlService;

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
        userDaoJdbc.setSqlService(sqlService);
        return userDaoJdbc;
    }
}
