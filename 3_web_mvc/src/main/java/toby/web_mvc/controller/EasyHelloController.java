package toby.web_mvc.controller;

import java.util.Map;

public class EasyHelloController extends EasyController {

    public EasyHelloController() {
        setRequiredParams(new String[]{"name"});
        setViewName("/WEB-INF/view/hello.jsp");
    }

    @Override
    public void control(Map<String, String> params, Map<String, Object> model) {
        model.put("message", "Hello " + params.get("name"));
    }
}
