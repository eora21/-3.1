import java.sql.SQLException;
import user.dao.NConnectionMaker;
import user.dao.UserDao;
import user.domain.User;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao(new NConnectionMaker());

        User user = new User();
        user.setId("eora21");
        user.setName("김주호");
        user.setPassword("pswd");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User getUser = dao.get(user.getId());
        System.out.println(getUser.getName());
        System.out.println(getUser.getPassword());

        System.out.println(getUser.getId() + " 조회 성공");
    }
}
