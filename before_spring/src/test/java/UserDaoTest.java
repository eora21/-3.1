import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import user.dao.UserDao;
import user.domain.User;

public class UserDaoTest {
    ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
    UserDao dao = context.getBean("userDao", UserDao.class);

    @BeforeEach
    void beforeEach() throws SQLException {
        dao.deleteAll();
    }

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
        User userA = new User("A", "에이", "PA");
        User userB = new User("B", "비", "PB");

        assertThat(dao.getCount()).isZero();

        dao.add(userA);
        dao.add(userB);
        assertThat(dao.getCount()).isEqualTo(2);

        User getUserA = dao.get(userA.getId());
        assertThat(userA.getName()).isEqualTo(getUserA.getName());
        assertThat(userA.getPassword()).isEqualTo(getUserA.getPassword());

        User getUserB = dao.get(userB.getId());
        assertThat(userB.getName()).isEqualTo(getUserB.getName());
        assertThat(userB.getPassword()).isEqualTo(getUserB.getPassword());
    }

    @Test
    void count() throws SQLException {
        User userA = new User("A", "에이", "PA");
        User userB = new User("B", "비", "PB");
        User userC = new User("C", "씨", "PC");

        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        dao.add(userA);
        assertThat(dao.getCount()).isOne();

        dao.add(userB);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(userC);
        assertThat(dao.getCount()).isEqualTo(3);
    }

    @Test
    void getUserFailure() {
        assertThatThrownBy(() -> dao.get("unknownId"))
                .isExactlyInstanceOf(EmptyResultDataAccessException.class);
    }
}
