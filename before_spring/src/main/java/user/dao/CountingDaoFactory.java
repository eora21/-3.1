package user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.dao.connection.ConnectionMaker;
import user.dao.connection.CountingConnectionMaker;
import user.dao.connection.NConnectionMaker;

@Configuration
public class CountingDaoFactory {
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
//        ConnectionMaker connectionMaker = connectionMaker();
//        userDao.setConnectionMaker(connectionMaker);
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new NConnectionMaker();
    }
}
