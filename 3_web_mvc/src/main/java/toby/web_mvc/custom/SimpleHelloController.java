package toby.web_mvc.custom;

import java.util.Map;

public class SimpleHelloController implements SimpleController {
    @ViewName("/WEB-INF/view/hello.jsp")
    @RequiredParams({"name"})
    @Override
    public void control(Map<String, String> params, Map<String, Object> model) {
        model.put("message", "Hello " + params.get("name"));
    }
}
