package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import user.dao.statement.StatementStrategy;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(final String query) throws SQLException {
        workWithStatementStrategy((Connection c) -> c.prepareStatement(query));
    }

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = strategy.makePreparedStatement(c)) {

            ps.executeUpdate();
        }
    }
}
