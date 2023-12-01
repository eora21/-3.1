package user.service;

import static user.domain.Level.BASIC;
import static user.domain.Level.GOLD;
import static user.domain.Level.SILVER;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

public class UserService {
    private final Map<Level, Predicate<User>> levelUpConditions = writeCondition();
    private final Map<Level, Level> nextLevelInfo = writeNextLevel();

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private Map<Level, Predicate<User>> writeCondition() {
        Map<Level, Predicate<User>> conditions = new EnumMap<>(Level.class);
        conditions.put(BASIC, user -> user.getLogin() >= 50);
        conditions.put(SILVER, user -> user.getRecommend() >= 30);
        conditions.put(GOLD, user -> false);

        assert conditions.size() == Level.values().length;
        return conditions;
    }

    private Map<Level, Level> writeNextLevel() {
        Map<Level, Level> nextLevel = new EnumMap<>(Level.class);
        nextLevel.put(BASIC, SILVER);
        nextLevel.put(SILVER, GOLD);
        nextLevel.put(GOLD, GOLD);

        assert nextLevel.size() == Level.values().length;
        return nextLevel;
    }

    public void upgradeLevels() {
        userDao.getAll().stream()
                .filter(this::canLevelUp)
                .forEach(this::levelUp);
    }

    private boolean canLevelUp(User user) {
        return levelUpConditions.get(user.getLevel())
                .test(user);
    }

    private void levelUp(User user) {
        Level nextLevel = nextLevelInfo.get(user.getLevel());
        user.setLevel(nextLevel);
        userDao.update(user);
    }
}
