import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import user.dao.UserDao;
import user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class UserDaoJdbcTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    UserDao dao;

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

    @Test
    void getAll() {
        User userA = new User("A", "에이", "PA");
        User userB = new User("B", "비", "PB");
        User userC = new User("C", "씨", "PC");

        List<User> users = dao.getAll();
        assertThat(users).isEmpty();

        dao.add(userB);
        users = dao.getAll();
        assertThat(users).hasSize(1);
        checkSameUser(userB, users.get(0));

        dao.add(userC);
        users = dao.getAll();
        assertThat(users).hasSize(2);
        checkSameUser(userB, users.get(0));
        checkSameUser(userC, users.get(1));

        dao.add(userA);
        users = dao.getAll();
        assertThat(users).hasSize(3);
        checkSameUser(userA, users.get(0));
        checkSameUser(userB, users.get(1));
        checkSameUser(userC, users.get(2));
    }

    private void checkSameUser(User firstUser, User secondUser) {
        assertAll(
                () -> assertThat(firstUser.getId()).isEqualTo(secondUser.getId()),
                () -> assertThat(firstUser.getName()).isEqualTo(secondUser.getName()),
                () -> assertThat(firstUser.getPassword()).isEqualTo(secondUser.getPassword())
        );
    }

    @Test
    void duplicateKey() {
        User userA = new User("A", "에이", "PA");
        assertDoesNotThrow(() -> dao.add(userA));
        assertThrows(DataAccessException.class, () -> dao.add(userA));
        assertThrowsExactly(DuplicateKeyException.class, () -> dao.add(userA));
    }
}
