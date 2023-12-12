package user.sqlservice;

public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException() {
        super("SQL을 찾을 수 없습니다. 등록된 SQL이 아닙니다.");
    }
}
