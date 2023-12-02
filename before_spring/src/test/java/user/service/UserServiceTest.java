package user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static user.domain.NormalLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static user.domain.NormalLevelUpgradePolicy.MIN_RECOMMEND_COUNT_FOR_GOLD;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.NormalLevelUpgradePolicy;
import user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @SpyBean
    NormalLevelUpgradePolicy userLevelUpgradePolicy;

    List<User> users = List.of(
            new User("basic", "브론즈", "password", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0),
            new User("toSilver", "브론즈->실버", "password", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0),
            new User("silver", "실버", "password", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD - 1),
            new User("toGold", "실버->골드", "password", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD),
            new User("gold", "골드", "password", Level.GOLD, 100, Integer.MAX_VALUE)
    );

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @Test
    void upgradeLevels() throws Exception {
        users.forEach(userDao::add);
        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }

    private void checkLevel(User user, boolean upgraded) {
        Level userLevel = user.getLevel();
        User userUpdate = userDao.get(user.getId());
        Level updateLevel = userUpdate.getLevel();

        if (upgraded) {
            assertThat(updateLevel).isEqualByComparingTo(userLevel.nextLevel());
            return;
        }

        assertThat(updateLevel).isEqualByComparingTo(userLevel);
    }

    @Test
    void add() {
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualByComparingTo(Level.GOLD);
        assertThat(userWithoutLevelRead.getLevel()).isEqualByComparingTo(Level.BASIC);
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        doAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            if (userArg.getId().equals("toSilver")) {
                throw new TestUserServiceException();
            }

            invocation.callRealMethod();

            return null;
        }).when(userLevelUpgradePolicy).upgradeLevel(any(User.class));

        try {
            users.forEach(userDao::add);
            userService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
            // ignore
        }

        checkLevel(users.get(3), false);
    }

    static class TestUserServiceException extends RuntimeException {
    }

}