package toby.web_mvc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@Setter
public abstract class EasyController implements Controller {
    private String[] requiredParams;
    private String viewName;

    @Override
    public final ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (Objects.isNull(viewName)) {
            throw new IllegalStateException();
        }

        Map<String, String> params = new HashMap<>();

        for (String requiredParam : requiredParams) {
            String value = request.getParameter(requiredParam);

            if (Objects.isNull(value)) {
                throw new IllegalStateException();
            }

            params.put(requiredParam, value);
        }

        Map<String, Object> model = new HashMap<>();
        this.control(params, model);
        return new ModelAndView(this.viewName, model);
    }

    public abstract void control(Map<String, String> params, Map<String, Object> model);
}
