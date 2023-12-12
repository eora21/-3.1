package user.service.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sqlmapType", propOrder = {"sql"})
@XmlRootElement(name = "sqlmap")
public class Sqlmap {
    @XmlElement(required = true)
    protected List<SqlType> sql;

    public List<SqlType> getSql() {
        if (Objects.isNull(sql)) {
            sql = new ArrayList<>();
        }

        return sql;
    }
}
