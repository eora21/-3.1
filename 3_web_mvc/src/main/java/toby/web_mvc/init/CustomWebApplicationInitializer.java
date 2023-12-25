package toby.web_mvc.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class CustomWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.scan("@Configuration annotation class");

        ContextLoaderListener listener = new ContextLoaderListener(rootContext);
        servletContext.addListener(listener);

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("spring", new DispatcherServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/app/*");
    }
}
