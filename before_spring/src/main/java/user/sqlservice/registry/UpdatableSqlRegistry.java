package user.sqlservice.registry;

import java.util.Map;
import user.sqlservice.exception.SqlUpdateFailureException;

public interface UpdatableSqlRegistry extends SqlRegistry {
    void updateSql(String key, String sql) throws SqlUpdateFailureException;

    void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
