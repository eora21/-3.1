package user.sqlservice.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import user.sqlservice.exception.SqlNotFoundException;
import user.sqlservice.exception.SqlUpdateFailureException;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
    private final Map<String, String> sqlMap = new ConcurrentHashMap<>();
    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);

        if (Objects.isNull(sql)) {
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if (sqlMap.containsKey(key)) {
            sqlMap.put(key, sql);
            return;
        }

        throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        sqlmap.forEach(this::updateSql);
    }
}
