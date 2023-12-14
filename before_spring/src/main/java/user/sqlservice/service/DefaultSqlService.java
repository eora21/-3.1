package user.sqlservice.service;

import user.sqlservice.registry.HashMapSqlRegistry;
import user.sqlservice.reader.JaxbXmlSqlReader;

public class DefaultSqlService extends BaseSqlService {
    public DefaultSqlService() {
        super.setSqlReader(new JaxbXmlSqlReader());
        super.setSqlRegistry(new HashMapSqlRegistry());
    }
}
