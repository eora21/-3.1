package user.domain;

import static user.domain.Level.BASIC;
import static user.domain.Level.GOLD;
import static user.domain.Level.SILVER;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import user.dao.UserDao;

public class NormalLevelUpgradePolicy implements UserLevelUpgradePolicy {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_COUNT_FOR_GOLD = 30;

    private final Map<Level, Predicate<User>> levelUpConditions = writeCondition();
    private final UserDao userDao;


    public NormalLevelUpgradePolicy(UserDao userDao) {
        this.userDao = userDao;
    }

    private Map<Level, Predicate<User>> writeCondition() {
        Map<Level, Predicate<User>> conditions = new EnumMap<>(Level.class);
        conditions.put(BASIC, user -> user.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER);
        conditions.put(SILVER, user -> user.getRecommend() >= MIN_RECOMMEND_COUNT_FOR_GOLD);
        conditions.put(GOLD, user -> false);

        assert conditions.size() == Level.values().length;
        return conditions;
    }

    @Override
    public boolean canUpgradeLevel(User user) {
        return levelUpConditions.get(user.getLevel())
                .test(user);
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(user.getEmail());
        mailMessage.setFrom("eora21@naver.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "으로 업그레이드 되었습니다.");

        mailSender.send(mailMessage);
    }
}
