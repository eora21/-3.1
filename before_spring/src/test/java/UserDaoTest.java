import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import user.dao.CountingConnectionMaker;
import user.dao.CountingDaoFactory;
import user.dao.DaoFactory;
import user.dao.UserDao;
import user.domain.User;

public class UserDaoTest {
    @Test
    void daoTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("eora21");
        user.setName("김주호");
        user.setPassword("pswd");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User getUser = dao.get(user.getId());
        System.out.println(getUser.getName());
        System.out.println(getUser.getPassword());

        System.out.println(getUser.getId() + " 조회 성공");
    }

    @Test
    void connectionCountingTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("eora21");
        user.setName("김주호");
        user.setPassword("pswd");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User getUser = dao.get(user.getId());
        System.out.println(getUser.getName());
        System.out.println(getUser.getPassword());

        System.out.println(getUser.getId() + " 조회 성공");

        CountingConnectionMaker connectionMaker =
                context.getBean("connectionMaker", CountingConnectionMaker.class);

        System.out.println("연결 횟수: " + connectionMaker.getCounter());
    }
}
