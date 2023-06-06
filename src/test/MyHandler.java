package test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class MyHandler extends AbstractHandler {

    private static final Logger LOG = Log.getLogger(MyHandler.class);

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String method = request.getMethod();

        switch (method) {
            case "GET":
                handleGet(request, response);
                break;
            case "POST":
                handlePost(request, response);
                break;
            case "PUT":
                handlePut(request, response);
                break;
            case "DELETE":
                handleDelete(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }

        baseRequest.setHandled(true);
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // GET 요청 처리 로직 구현
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("GET request processed");
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // POST 요청 처리 로직 구현
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("POST request processed");
    }

    private void handlePut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // PUT 요청 처리 로직 구현
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("PUT request processed");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // DELETE 요청 처리 로직 구현
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("DELETE request processed");
    }

}
