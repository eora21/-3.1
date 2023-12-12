package learningtest.jdk.jaxb;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.util.List;
import org.junit.jupiter.api.Test;
import user.service.jaxb.SqlType;
import user.service.jaxb.Sqlmap;

public class JaxbTest {
    @Test
    void readSqlmap() throws Exception {
        JAXBContext context = JAXBContext.newInstance(Sqlmap.class, SqlType.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("/sqlmaptest.xml"));

        List<SqlType> sqlList = sqlmap.getSql();

        assertThat(sqlList.size()).isEqualTo(3);
        assertThat(sqlList.get(0).getKey()).isEqualTo("add");
        assertThat(sqlList.get(0).getValue()).isEqualTo("insert");
        assertThat(sqlList.get(1).getKey()).isEqualTo("get");
        assertThat(sqlList.get(1).getValue()).isEqualTo("select");
        assertThat(sqlList.get(2).getKey()).isEqualTo("delete");
        assertThat(sqlList.get(2).getValue()).isEqualTo("delete");
    }
}
