import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import info.Info;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import user.dao.UserDao;
import user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@DirtiesContext
public class UserDaoTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    UserDao dao;

    @BeforeEach
    void beforeEach() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/toby_spring",
                Info.MYSQL_ID.getValue(), Info.MYSQL_PASSWORD.getValue(), true);
        dao.setDataSource(dataSource);
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
