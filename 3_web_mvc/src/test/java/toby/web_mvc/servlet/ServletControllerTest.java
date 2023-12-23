package toby.web_mvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;

public class ServletControllerTest extends AbstractDispatcherServletTest {
    @Component("/hello")
    static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String name = req.getParameter("name");
            resp.getWriter().print("Hello " + name);
        }
    }

    @Test
    void helloServletController() throws Exception {
        setClasses(SimpleServletHandlerAdapter.class, HelloServlet.class);
        initRequest("/hello").addParameter("name", "Spring");

        assertThat(runService().getContentAsString()).isEqualTo("Hello Spring");  // jdk 버전에 의한 오류
    }
}
