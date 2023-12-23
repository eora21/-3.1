package toby.web_mvc.servlet;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

@NoArgsConstructor
public class ConfigurableDispatcherServlet extends DispatcherServlet {
    private String[] locations;
    private Class<?>[] classes;
    @Getter
    private ModelAndView modelAndView;

    public ConfigurableDispatcherServlet(String[] locations) {
        this.locations = locations;
    }

    public ConfigurableDispatcherServlet(Class<?>[] classes) {
        this.classes = classes;
    }

    public void setLocations(String ...locations) {
        this.locations = locations;
    }

    public void setRelativeLocations(Class<?> clazz, String... relativeLocations) {
        String[] locations = new String[relativeLocations.length];
        String currentPath = ClassUtils.classPackageAsResourcePath(clazz) + "/";

        for (int i = 0; i < relativeLocations.length; i++) {
            locations[i] = currentPath + relativeLocations[i];
        }

        setLocations(locations);
    }

    public void setClasses(Class<?> ...classes) {
        this.classes = classes;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        modelAndView = null;
        super.service(req, res);
    }

    @Override
    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        AbstractRefreshableWebApplicationContext webApplicationContext =
                new AbstractRefreshableWebApplicationContext() {
            @Override
            protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
                    throws BeansException, IOException {
                if (Objects.nonNull(locations)) {
                    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beanFactory);
                    xmlReader.loadBeanDefinitions(locations);
                }

                if (Objects.nonNull(classes)) {
                    AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
                    reader.register(classes);
                }
            }
        };

        webApplicationContext.setServletContext(getServletContext());
        webApplicationContext.setServletConfig(getServletConfig());
        webApplicationContext.refresh();

        return webApplicationContext;
    }

    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.modelAndView = mv;
        super.render(mv, request, response);
    }

}
