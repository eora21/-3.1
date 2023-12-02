package user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Objects;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void upgradeLevel() {
        for (Level level : Level.values()) {
            if (Objects.isNull(level.nextLevel())) {
                continue;
            }

            User user = new User();
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test
    void cannotUpgradeLevel() {
        for (Level level : Level.values()) {
            if (Objects.nonNull(level.nextLevel())) {
                continue;
            }

            User user = new User();
            user.setLevel(level);

            assertThatThrownBy(user::upgradeLevel)
                    .isExactlyInstanceOf(IllegalStateException.class);
        }
    }
}