package test.http;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyRequestHandlerPath03 extends AbstractHandler {
    @Override
    public void handle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {
        String pathName = target.substring("/path01/".length());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(pathName + " got it!");
    }
}