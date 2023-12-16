package context;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:testApplicationContext.xml")
public class TestApplicationContext {

}
