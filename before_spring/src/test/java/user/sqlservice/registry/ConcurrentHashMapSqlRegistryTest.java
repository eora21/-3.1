package user.sqlservice.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import user.sqlservice.exception.SqlNotFoundException;
import user.sqlservice.exception.SqlUpdateFailureException;

class ConcurrentHashMapSqlRegistryTest {
    UpdatableSqlRegistry sqlRegistry = new ConcurrentHashMapSqlRegistry();

    @BeforeEach
    void setUp() {
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    @ParameterizedTest
    @CsvSource({"KEY1, SQL1", "KEY2, SQL2", "KEY3, SQL3"})
    void find(String key, String expected) {
        assertThat(sqlRegistry.findSql(key)).isEqualTo(expected);
    }

    @Test
    void unknownKey() {
        assertThatThrownBy(() -> sqlRegistry.findSql("JFO@#IRFJIS"))
                .isExactlyInstanceOf(SqlNotFoundException.class);
    }

    @Test
    void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo("SQL1");
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo("Modified2");
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo("SQL3");
    }

    @Test
    void updateMulti() {
        HashMap<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);

        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo("Modified1");
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo("SQL2");
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo("Modified3");
    }

    @Test
    void updateWithNotExistingKey() {
        assertThatThrownBy(() -> sqlRegistry.updateSql("JFO@#IRFJIS", "Modified"))
                .isExactlyInstanceOf(SqlUpdateFailureException.class);
    }
}