package user.service;

import static user.domain.Level.BASIC;

import java.util.Objects;
import user.dao.UserDao;
import user.domain.User;
import user.domain.UserLevelUpgradePolicy;

public class UserService {
    UserLevelUpgradePolicy levelUpgradePolicy;
    UserDao userDao;

    public void setLevelUpgradePolicy(UserLevelUpgradePolicy levelUpgradePolicy) {
        this.levelUpgradePolicy = levelUpgradePolicy;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        userDao.getAll().stream()
                .filter(levelUpgradePolicy::canUpgradeLevel)
                .forEach(levelUpgradePolicy::upgradeLevel);
    }

    public void add(User user) {
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(BASIC);
        }

        userDao.add(user);
    }
}
