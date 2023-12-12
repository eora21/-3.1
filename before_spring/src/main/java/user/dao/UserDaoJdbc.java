package user.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import user.domain.Level;
import user.domain.User;

public class UserDaoJdbc implements UserDao {
    private final RowMapper<User> userMapper = (ResultSet rs, int rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        user.setEmail(rs.getString("email"));

        return user;
    };

    private Map<String, String> sqlMap;

    private JdbcTemplate jdbcTemplate;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        jdbcTemplate.update(
                sqlMap.get("add"),
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
                user.getRecommend(), user.getEmail());
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlMap.get("get"), new Object[] {id}, userMapper);
    }

    public void deleteAll() {
        jdbcTemplate.update(sqlMap.get("deleteAll"));
    }

    public int getCount() {
        return jdbcTemplate.queryForObject(sqlMap.get("getCount"), int.class);
    }

    public List<User> getAll() {
        return jdbcTemplate.query(sqlMap.get("getAll"), userMapper);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                sqlMap.get("update"),
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
                user.getEmail(), user.getId());
    }
}
