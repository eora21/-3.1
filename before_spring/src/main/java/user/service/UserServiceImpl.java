package user.service;

import static user.domain.Level.BASIC;

import java.util.List;
import java.util.Objects;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import user.dao.UserDao;
import user.domain.User;
import user.domain.UserLevelUpgradePolicy;

public class UserServiceImpl implements UserService {
    private UserLevelUpgradePolicy levelUpgradePolicy;
    private UserDao userDao;


    public void setLevelUpgradePolicy(UserLevelUpgradePolicy levelUpgradePolicy) {
        this.levelUpgradePolicy = levelUpgradePolicy;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add(User user) {
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(BASIC);
        }

        userDao.add(user);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void upgradeLevels() {
        userDao.getAll().stream()
                .filter(levelUpgradePolicy::canUpgradeLevel)
                .forEach(levelUpgradePolicy::upgradeLevel);
    }
}
