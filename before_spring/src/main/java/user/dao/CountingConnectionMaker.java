package user.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class CountingConnectionMaker implements ConnectionMaker {
    private final ConnectionMaker realConnectionMaker;
    private final AtomicInteger connectionCount = new AtomicInteger();

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        connectionCount.incrementAndGet();
        return realConnectionMaker.makeNewConnection();
    }

    public int getCounter() {
        return connectionCount.get();
    }
}
