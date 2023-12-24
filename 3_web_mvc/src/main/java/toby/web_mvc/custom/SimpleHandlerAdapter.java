package toby.web_mvc.custom;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

public class SimpleHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof SimpleController;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Method method = ReflectionUtils.findMethod(handler.getClass(), "control", Map.class, Map.class);
        RequiredParams requiredParams = AnnotationUtils.getAnnotation(method, RequiredParams.class);

        Map<String, String> params = new HashMap<>();
        for (String requireParam : requiredParams.value()) {
            String value = request.getParameter(requireParam);

            if (Objects.isNull(value)) {
                throw new IllegalStateException();
            }

            params.put(requireParam, value);
        }

        Map<String, Object> model = new HashMap<>();
        ((SimpleController) handler).control(params, model);

        ViewName viewName = AnnotationUtils.getAnnotation(method, ViewName.class);

        return new ModelAndView(viewName.value(), model);
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1;
    }
}
