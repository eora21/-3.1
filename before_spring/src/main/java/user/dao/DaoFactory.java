package user.dao;

public class DaoFactory {
    public UserDao userDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        return new UserDao(connectionMaker);
    }

    public ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }
}
