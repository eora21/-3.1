package user.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;

    List<User> users = List.of(
            new User("basic", "브론즈", "password", Level.BASIC, 49, 0),
            new User("toSilver", "브론즈-실버", "password", Level.BASIC, 50, 0),
            new User("silver", "실버", "password", Level.SILVER, 60, 29),
            new User("toGold", "실버-골드", "password", Level.SILVER, 60, 30),
            new User("gold", "골드", "password", Level.GOLD, 100, 100)
    );;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
        users.forEach(userDao::add);
    }

    @Test
    void upgradeLevels() {
        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualByComparingTo(level);
    }
}