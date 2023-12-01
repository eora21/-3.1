import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class UserDaoTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    UserDao dao;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    void beforeEach() throws SQLException {
        dao.deleteAll();
    }

    User userA = new User("A", "에이", "PA", Level.BASIC, 1, 0);
    User userB = new User("B", "비", "PB", Level.SILVER, 55, 10);
    User userC = new User("C", "씨", "PC", Level.GOLD, 100, 40);

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
        assertThat(dao.getCount()).isZero();

        dao.add(userA);
        dao.add(userB);
        assertThat(dao.getCount()).isEqualTo(2);

        User getUserA = dao.get(userA.getId());
        checkSameUser(userA, getUserA);

        User getUserB = dao.get(userB.getId());
        checkSameUser(userB, getUserB);
    }

    @Test
    void count() throws SQLException {
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
                () -> assertThat(firstUser.getPassword()).isEqualTo(secondUser.getPassword()),
                () -> assertThat(firstUser.getLevel()).isEqualTo(secondUser.getLevel()),
                () -> assertThat(firstUser.getLogin()).isEqualTo(secondUser.getLogin()),
                () -> assertThat(firstUser.getRecommend()).isEqualTo(secondUser.getRecommend())
        );
    }

    @Test
    void duplicateKey() {
        assertDoesNotThrow(() -> dao.add(userA));
        assertThrows(DataAccessException.class, () -> dao.add(userA));
        assertThrowsExactly(DuplicateKeyException.class, () -> dao.add(userA));
    }

    @Test
    void sqlExceptionTranslate() {
        try {
            dao.add(userA);
            dao.add(userA);
        } catch (DuplicateKeyException duplicateKeyException) {
            SQLException sqlException = (SQLException) duplicateKeyException.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlException)).isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Test
    void update() {
        dao.add(userA);
        dao.add(userB);

        userA.setName("수정");
        userA.setPassword("update");
        userA.setLevel(Level.GOLD);
        userA.setLogin(1_000);
        userA.setRecommend(999);
        dao.update(userA);

        User updateUserA = dao.get(userA.getId());
        checkSameUser(userA, updateUserA);

        User sameUserB = dao.get(userB.getId());
        checkSameUser(userB, sameUserB);
    }
}
