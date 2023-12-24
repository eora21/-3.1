package toby.web_mvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import toby.web_mvc.HelloSpring;

public class ExtendServletTest extends AbstractDispatcherServletTest {
    @Test
    void helloController() throws Exception {
        ModelAndView mav = setLocations("spring-servlet.xml")
                .setClasses(HelloSpring.class)
                .initRequest("/hello", RequestMethod.GET)
                .addParameter("name", "Spring")
                .runService()
                .getModelAndView();

        assertThat(mav.getViewName()).isEqualTo("/WEB-INF/view/hello.jsp");
        assertThat(mav.getModel().get("message")).isEqualTo("Hello Spring");
    }
}
