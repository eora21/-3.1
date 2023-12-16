package user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static user.domain.NormalLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static user.domain.NormalLevelUpgradePolicy.MIN_RECOMMEND_COUNT_FOR_GOLD;

import context.AppContext;
import context.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;
import user.domain.UserLevelUpgradePolicy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppContext.class, TestAppContext.class})
public class UserServiceTest {
    @Autowired
    UserService userService;
    @SpyBean
    UserDao userDao;
    @SpyBean
    UserLevelUpgradePolicy userLevelUpgradePolicy;
    @MockBean
    MailSender mailSender;
    @Autowired
    UserService testUserService;

    List<User> users = List.of(
            new User("basic", "브론즈", "password", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0, "basic@email"),
            new User("toSilver", "브론즈->실버", "password", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0, "toSilver@email"),
            new User("silver", "실버", "password", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD - 1, "silver@email"),
            new User("toGold", "실버->골드", "password", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD, "toGold@email"),
            new User("gold", "골드", "password", Level.GOLD, 100, Integer.MAX_VALUE, "gold@email")
    );

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @Test
    void upgradeLevels() throws Exception {
        List<String> requests = new ArrayList<>();

        doAnswer(invocation -> {
            SimpleMailMessage mailMessage = invocation.getArgument(0);
            requests.add(Objects.requireNonNull(mailMessage.getTo())[0]);

            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        when(userDao.getAll())
                .thenReturn(users);

        userService.upgradeLevels();

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
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

    @Test
    void advisorAutoProxyCreator() {
        assertThat(userService).isInstanceOf(java.lang.reflect.Proxy.class);
    }

    public static class TestUserService extends UserServiceImpl {
        @Override
        public List<User> getAll() {
            super.getAll()
                    .forEach(super::update);  // readOnly가 적용되나 Vendor에 따라 예외가 발생할 수도, 하지 않을 수도 있음.

            return null;
        }

        @Override
        public void add(User user) {
            System.out.println(TransactionSynchronizationManager.getCurrentTransactionName());
            super.add(user);
        }

        @Override
        public void deleteAll() {
            System.out.println(TransactionSynchronizationManager.getCurrentTransactionName());
            super.deleteAll();
        }
    }

    @Test
    @Transactional
    void transactionSync() {
        testUserService.deleteAll();
        testUserService.add(users.get(0));
    }
}