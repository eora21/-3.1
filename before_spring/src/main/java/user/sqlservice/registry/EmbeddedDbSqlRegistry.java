package user.sqlservice.registry;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import user.sqlservice.exception.SqlNotFoundException;
import user.sqlservice.exception.SqlUpdateFailureException;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    private JdbcTemplate jdbc;
    private TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    @Override
    public void registerSql(String key, String sql) {
        jdbc.update("INSERT INTO sqlmap(key_, sql_) VALUES (?, ?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return jdbc.queryForObject("select sql_ from sqlmap where key_ = ?", String.class, key);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다.", e);
        }
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = jdbc.update("update sqlmap set sql_ = ? where key_ = ?", sql, key);

        if (affected == 0) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
        transactionTemplate.execute(status -> {
            sqlmap.forEach(this::updateSql);
            return null;
        });
    }
}
