package user.dao;

import java.sql.ResultSet;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import user.domain.Level;
import user.domain.User;
import user.sqlservice.service.SqlService;

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

    private SqlService sqlService;

    private JdbcTemplate jdbcTemplate;

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }
    @Autowired @Qualifier("dataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        jdbcTemplate.update(
                sqlService.getSql("userAdd"),
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
                user.getRecommend(), user.getEmail());
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGet"), new Object[] {id}, userMapper);
    }

    public void deleteAll() {
        jdbcTemplate.update(sqlService.getSql("userDeleteAll"));
    }

    public int getCount() {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGetCount"), int.class);
    }

    public List<User> getAll() {
        return jdbcTemplate.query(sqlService.getSql("userGetAll"), userMapper);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                sqlService.getSql("userUpdate"),
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
                user.getEmail(), user.getId());
    }
}
