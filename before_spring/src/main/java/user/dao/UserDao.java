package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import user.dao.statement.AddStatement;
import user.dao.statement.DeleteAllStatement;
import user.dao.statement.StatementStrategy;
import user.domain.User;

public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        jdbcContextWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");

            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            return ps;
        });
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = strategy.makePreparedStatement(c)) {

            ps.executeUpdate();
        }
    }

    public User get(String id) throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("select * from users where id = ?")){
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EmptyResultDataAccessException(1);
                }

                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));

                return user;
            }
        }
    }

    public void deleteAll() throws SQLException {
        jdbcContextWithStatementStrategy(c -> c.prepareStatement("delete from users"));
    }

    public int getCount() throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("select count(*) from users");
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);
        }
    }
}
