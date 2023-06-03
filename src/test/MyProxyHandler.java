package test;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

// AbstractHandler 클래스를 상속받아서 구현합니다.
// AbstractHandler 클래스는 Jetty 서버가 요청을 처리하는 방법을 정의하는 클래스입니다.
public class MyProxyHandler extends AbstractHandler {

    private final Logger logger = Log.getLogger(getClass());

    // AbstractHandler.handle() 메소드를 오버라이딩해서 구현합니다.
    // 모든 HTTP 요청이 이 메소드를 통해 처리됩니다.
    @Override
    public void handle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {

        HttpURLConnection connection = null;

        try {
            // 주어진 target URL로 HTTP 연결하는 HttpURLConnection 객체를 생성합니다.
            URL url = new URL(target);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod());

            // 들어오는 요청의 헤더를 복사합니다.
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                connection.setRequestProperty(headerName, headerValue);
            }

            // HttpURLConnection 객체에게 출력을 사용할 것을 알려줍니다.
            // 이렇게 하면 connection.getOutputStream() 메소드를 호출할 수 있습니다.
            // 이 메소드를 호출하면 HttpURLConnection 객체는 내부적으로
            // HTTP 요청의 헤더를 보내고, HTTP 요청의 본문을 보낼 수 있습니다.
            // 즉, HttpURLConnection 객체에게 요청 본문을 쓸 것임을 지정하는 역할을 합니다.
            connection.setDoOutput(true);

            // 들어오는 요청의 본문을 복사합니다.
            ServletInputStream inputStream = request.getInputStream();
            OutputStream outputStream = connection.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            // 연결된 HttpURLConnection 객체에게 요청을 보내고, 응답을 받습니다.
            response.setStatus(connection.getResponseCode());
            // 응답의 헤더를 복사합니다.
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                String headerName = header.getKey();
                if (headerName != null) {
                    for (String headerValue : header.getValue()) {
                        response.addHeader(headerName, headerValue);
                    }
                }
            }

            // 연결된 HttpURLConnection 객체로부터 응답 본문을 읽어서
            // 들어오는 요청의 응답 본문으로 복사합니다.
            InputStream responseInputStream = connection.getInputStream();
            ServletOutputStream responseOutputStream = response.getOutputStream();
            byte[] responseBuffer = new byte[4096];
            int responseBytesRead;
            while ((responseBytesRead = responseInputStream.read(responseBuffer)) != -1) {
                responseOutputStream.write(responseBuffer, 0, responseBytesRead);
            }
            responseOutputStream.close();
            responseInputStream.close();

            // 요청을 처리했음을 Jetty 서버에게 알려줍니다.
            // 이렇게 하면 요청이 다른 핸들러나 서블릿으로 전달되지 않고, 여기서 끝납니다.
            baseRequest.setHandled(true);

        } catch (Exception e) {
            logger.warn("Proxy error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            baseRequest.setHandled(true);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
