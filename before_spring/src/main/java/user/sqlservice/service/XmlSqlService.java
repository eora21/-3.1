package user.sqlservice.service;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import user.sqlservice.exception.SqlNotFoundException;
import user.sqlservice.reader.SqlReader;
import user.sqlservice.exception.SqlRetrievalFailureException;
import user.sqlservice.jaxb.SqlType;
import user.sqlservice.jaxb.Sqlmap;
import user.sqlservice.registry.SqlRegistry;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {
    private final Map<String, String> sqlMap = new HashMap<>();
    private String sqlmapFile;
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);

        if (Objects.isNull(sql)) {
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class, SqlType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                    getClass().getClassLoader().getResourceAsStream(sqlmapFile));

            for (SqlType sql : sqlmap.getSql()) {
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
