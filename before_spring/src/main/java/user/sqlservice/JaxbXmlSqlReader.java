package user.sqlservice;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import user.service.jaxb.SqlType;
import user.service.jaxb.Sqlmap;

public class JaxbXmlSqlReader implements SqlReader {
    private String sqlmapFile;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
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
