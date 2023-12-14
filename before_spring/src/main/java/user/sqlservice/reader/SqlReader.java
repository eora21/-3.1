package user.sqlservice.reader;

import user.sqlservice.registry.SqlRegistry;

public interface SqlReader {
    void read(SqlRegistry sqlRegistry);
}
