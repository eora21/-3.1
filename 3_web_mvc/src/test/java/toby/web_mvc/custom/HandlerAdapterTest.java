package toby.web_mvc.custom;

import org.junit.jupiter.api.Test;
import toby.web_mvc.servlet.AbstractDispatcherServletTest;

class HandlerAdapterTest extends AbstractDispatcherServletTest {
    @Test
    void simpleHandlerAdapter() throws Exception {
        setClasses(SimpleHandlerAdapter.class, SimpleHelloController.class);
        initRequest("/hello").addParameter("name", "Spring").runService();

        assertViewName("/WEB-INF/view/hello.jsp");
        assertModel("message", "Hello Spring");
    }
}