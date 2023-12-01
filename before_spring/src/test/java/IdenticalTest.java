import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import user.dao.DaoFactory;
import user.dao.UserDaoJdbc;

public class IdenticalTest {
    @Test
    void daoFactoryIdenticalTest() {
        DaoFactory factory = new DaoFactory();
        UserDaoJdbc dao1 = factory.userDao();
        UserDaoJdbc dao2 = factory.userDao();

        System.out.println(dao1);
        System.out.println(dao2);
        System.out.println(dao1 == dao2);
    }

    @Test
    void SpringContextBeanIdenticalTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDaoJdbc dao1 = context.getBean("userDao", UserDaoJdbc.class);
        UserDaoJdbc dao2 = context.getBean("userDao", UserDaoJdbc.class);

        System.out.println(dao1);
        System.out.println(dao2);
        System.out.println(dao1 == dao2);
    }
}
