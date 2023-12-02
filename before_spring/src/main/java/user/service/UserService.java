package user.service;

import static user.domain.Level.BASIC;

import java.sql.Connection;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import user.dao.UserDao;
import user.domain.User;
import user.domain.UserLevelUpgradePolicy;

public class UserService {
    UserLevelUpgradePolicy levelUpgradePolicy;
    UserDao userDao;
    DataSource dataSource;

    public void setLevelUpgradePolicy(UserLevelUpgradePolicy levelUpgradePolicy) {
        this.levelUpgradePolicy = levelUpgradePolicy;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void upgradeLevels() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setAutoCommit(false);

        try {
            userDao.getAll().stream()
                    .filter(levelUpgradePolicy::canUpgradeLevel)
                    .forEach(levelUpgradePolicy::upgradeLevel);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    public void add(User user) {
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(BASIC);
        }

        userDao.add(user);
    }
}
