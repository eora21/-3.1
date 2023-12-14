package user.sqlservice.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import user.sqlservice.reader.SqlReader;
import user.sqlservice.exception.SqlRetrievalFailureException;
import user.sqlservice.jaxb.SqlType;
import user.sqlservice.jaxb.Sqlmap;
import user.sqlservice.registry.HashMapSqlRegistry;
import user.sqlservice.registry.SqlRegistry;

public class OxmSqlService implements SqlService {
    private final BaseSqlService baseSqlService = new BaseSqlService();
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlmap(Resource sqlmap) {
        oxmSqlReader.setSqlmap(sqlmap);
    }

    @PostConstruct
    public void loadSql() {
        baseSqlService.setSqlReader(oxmSqlReader);
        baseSqlService.setSqlRegistry(sqlRegistry);

        baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return baseSqlService.getSql(key);
    }

    private static class OxmSqlReader implements SqlReader {
        private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
        private Unmarshaller unmarshaller;
        private Resource sqlmap = new ClassPathResource(DEFAULT_SQLMAP_FILE);

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new StreamSource(this.sqlmap.getInputStream()));

                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmap.getFilename() + "을 가져올 수 없습니다", e);
            }
        }
    }
}
