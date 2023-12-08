package user.service;

import static user.domain.Level.BASIC;

import java.util.Objects;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import user.dao.UserDao;
import user.domain.User;
import user.domain.UserLevelUpgradePolicy;

public class UserService {
    private UserLevelUpgradePolicy levelUpgradePolicy;
    private UserDao userDao;
    private PlatformTransactionManager transactionManager;

    public void setLevelUpgradePolicy(UserLevelUpgradePolicy levelUpgradePolicy) {
        this.levelUpgradePolicy = levelUpgradePolicy;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userDao.getAll().stream()
                    .filter(levelUpgradePolicy::canUpgradeLevel)
                    .forEach(levelUpgradePolicy::upgradeLevel);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    public void add(User user) {
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(BASIC);
        }

        userDao.add(user);
    }
}
