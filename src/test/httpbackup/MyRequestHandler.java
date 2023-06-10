package test.httpbackup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

// AbstractHandler 클래스를 상속받아서 구현합니다.
// AbstractHandler 클래스는 Jetty 서버가 요청을 처리하는 방법을 정의하는 클래스입니다.
public class MyRequestHandler extends AbstractHandler {

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
        // HTTP METHOD를 출력합니다.
        logger.info("HTTP METHOD: " + request.getMethod());
        // HTTP 요청의 경로를 출력합니다.
        logger.info("HTTP PATH: " + target);
        // HTTP 요청의 헤더를 출력합니다.
        logger.info("HTTP HEADER:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info(headerName + ": " + request.getHeader(headerName));
        }
        // HTTP 요청의 본문을 출력합니다.
        String requestBody = readRequestBody(request);
        logger.info(requestBody);
        // HTTP 요청의 본문을 JSON 객체로 파싱합니다.
        Gson gson = new Gson();
        JsonObject requestBodyToJsonObject = gson.fromJson(requestBody, JsonObject.class);
        logger.info("Parsed Request Model: " + requestBodyToJsonObject);

        // HTTP 요청의 경로에 따라서 다른 핸들러를 호출합니다.
        // HTTP Method에 따라서 다른 핸들러를 호출하게 바꿀 수도 있습니다.
        if (target.startsWith("/path01/")) {
            MyRequestHandlerPath01 handlePath01 = new MyRequestHandlerPath01();
            handlePath01.handle(target, baseRequest, request, response);
        } else if (target.startsWith("/path02/")) {
            MyRequestHandlerPath02 handlePath02 = new MyRequestHandlerPath02();
            handlePath02.handle(target, baseRequest, request, response);
        } else if (target.startsWith("/path03/")) {
            MyRequestHandlerPath03 handlePath03 = new MyRequestHandlerPath03();
            handlePath03.handle(target, baseRequest, request, response);
        } else {
            // HTTP 요청의 경로가 /path01/, /path02/, /path03/로 시작하지 않는 경우에는
            // response 본문에 샘플 JSON 데이터를 담아서 보냅니다.
            JsonObject jo = new Gson().fromJson("{\"test\": \"value\"}", JsonObject.class);
            // 응답의 본문으로 사용할 JSON 문자열 생성
            String responseBody = jo.toString();
            // 응답 설정
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(responseBody); // 응답 본문 설정
        }
        // HTTP 요청을 처리했음을 표시하여, 다른 핸들러가 요청을 처리하지 않도록 합니다.
        baseRequest.setHandled(true);
    }

    // HTTP 요청의 본문을 읽어서 문자열로 반환합니다.
    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        ServletInputStream inputStream = request.getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }

}
