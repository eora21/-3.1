package user.dao.connection;

import info.Info;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/toby_spring",
                Info.MYSQL_ID.getValue(), Info.MYSQL_PASSWORD.getValue());
    }
}
