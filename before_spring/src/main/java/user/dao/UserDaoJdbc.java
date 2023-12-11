package user.dao;

import java.sql.ResultSet;
import java.util.List;
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

    private String sqlAdd;
    private String sqlGet;
    private String sqlDeleteAll;
    private String sqlGetCount;
    private String sqlGetAll;
    private String sqlUpdate;

    public void setSqlAdd(String sqlAdd) {
        this.sqlAdd = sqlAdd;
    }

    public void setSqlGet(String sqlGet) {
        this.sqlGet = sqlGet;
    }

    public void setSqlDeleteAll(String sqlDeleteAll) {
        this.sqlDeleteAll = sqlDeleteAll;
    }

    public void setSqlGetCount(String sqlGetCount) {
        this.sqlGetCount = sqlGetCount;
    }

    public void setSqlGetAll(String sqlGetAll) {
        this.sqlGetAll = sqlGetAll;
    }

    public void setSqlUpdate(String sqlUpdate) {
        this.sqlUpdate = sqlUpdate;
    }

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        jdbcTemplate.update(
                sqlAdd,
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
                user.getRecommend(), user.getEmail());
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlGet, new Object[] {id}, userMapper);
    }

    public void deleteAll() {
        jdbcTemplate.update(sqlDeleteAll);
    }

    public int getCount() {
        return jdbcTemplate.queryForObject(sqlGetCount, int.class);
    }

    public List<User> getAll() {
        return jdbcTemplate.query(sqlGetAll, userMapper);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                sqlUpdate,
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
                user.getEmail(), user.getId());
    }
}
