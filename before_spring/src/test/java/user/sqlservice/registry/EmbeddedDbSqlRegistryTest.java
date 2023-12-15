package user.sqlservice.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import user.sqlservice.exception.SqlUpdateFailureException;

class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @Test
    void transactionalUpdate() {
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo("SQL1");
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo("SQL2");
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo("SQL3");

        Map<String, String> sqlmap = Map.of(
                "KEY1", "Modified1",
                "KEY9999!@#$", "Modified9999"
        );

        try {
            sqlRegistry.updateSql(sqlmap);
            fail("SqlUpdateFailureException expected");
        } catch (SqlUpdateFailureException e) {
            //ignore
        }

        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo("SQL1");
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo("SQL2");
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo("SQL3");
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }
}