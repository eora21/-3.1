package learningtest.oxm;

import static org.assertj.core.api.Assertions.assertThat;

import context.AppContext;
import context.TestAppContext;
import java.io.IOException;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import user.domain.UserLevelUpgradePolicy;
import user.sqlservice.jaxb.SqlType;
import user.sqlservice.jaxb.Sqlmap;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppContext.class)
@ActiveProfiles("local")
public class OxmTest {
    @Autowired
    Unmarshaller unmarshaller;

    @Test
    void unmarshallSqlMap() throws XmlMappingException, IOException {
        StreamSource xmlSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("sqlmaptest.xml"));

        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

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
