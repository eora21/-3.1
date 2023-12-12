package user.sqlservice;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import user.service.jaxb.SqlType;
import user.service.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {
    private final Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() {
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class, SqlType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                    getClass().getClassLoader().getResourceAsStream("sqlmap.xml"));

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);

        if (Objects.isNull(sql)) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }
}
