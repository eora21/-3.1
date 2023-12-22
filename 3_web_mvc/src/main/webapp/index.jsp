<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="toby.web_mvc.HelloSpring" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<%
    ApplicationContext context =
            WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
    HelloSpring helloSpring = context.getBean(HelloSpring.class);

    out.println(helloSpring.sayHello("Root Context"));
%>
</body>
</html>