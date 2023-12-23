package toby.web_mvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.ModelAndView;
import toby.web_mvc.HelloSpring;

class ConfigurableDispatcherServletTest {
    @Test
    void configurableDispatcherServletTest() throws Exception {
        ConfigurableDispatcherServlet servlet = new ConfigurableDispatcherServlet();
        servlet.setLocations("spring-servlet.xml");
        servlet.setClasses(HelloSpring.class);

        servlet.init(new MockServletConfig("spring"));

        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
        req.addParameter("name", "Spring");
        MockHttpServletResponse res = new MockHttpServletResponse();

        servlet.service(req, res);

        ModelAndView mav = servlet.getModelAndView();
        assertThat(mav.getViewName()).isEqualTo("/WEB-INF/view/hello.jsp");
        assertThat(mav.getModel().get("message")).isEqualTo("Hello Spring");
    }
}