package user.sqlservice.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class UserSqlMapConfig implements SqlMapConfig {
    @Override
    public Resource getSqlMApResource() {
        return new ClassPathResource("sqlmap.xml");
    }
}
