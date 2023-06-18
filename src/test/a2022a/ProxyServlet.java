package test.a2022a;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class ProxyServlet extends HttpServlet {

    private String targetPath = "";

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public void init() throws ServletException {
        // 서블릿 초기화 시 필요한 작업이 있으면 여기서 처리합니다.
        // 이 메소드는 서블릿이 최초로 실행될 때 한 번만 실행됩니다.
        // 서블릿이 실행되는 동안 필요한 작업이 없으면 이 메소드는 비워둡니다.
        System.out.println("ProxyServlet init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        proxyRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        proxyRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        proxyRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        proxyRequest(req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 요청된 HTTP Method, Path, Query를 그대로 전달하기 위해 service() 메소드를 오버라이드합니다.
        // super.service()를 호출하지 않고 proxyRequest() 메소드만 호출합니다.
        proxyRequest(req, resp);
    }

    private void proxyRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestMethod = req.getMethod();
        String targetUrl = createTargetUrl(req);

        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);

        // 원본 요청 헤더를 프록시 요청에 복사
        copyRequestHeaders(req, con);

        // 원본 요청 파라미터를 프록시 요청에 복사
        copyRequestParameters(req, con);

        // 원본 요청 바디를 프록시 요청에 복사
        copyRequestBody(req, con);

        // 프록시 요청을 보내고 응답을 받습니다.
        int responseCode = con.getResponseCode();

        // 프록시 응답 헤더를 원본 응답에 복사
        copyResponseHeaders(con, resp);

        // 프록시 응답 파라미터를 원본 응답에 복사
        copyResponseParameters(con, resp);

        // 프록시 응답 바디를 원본 응답에 복사
        copyResponseBody(con, resp);

        // 프록시 응답 코드를 원본 응답에 복사
        resp.setStatus(responseCode);

        // 프록시 응답을 보냈으면 연결을 닫습니다.
        con.disconnect();
    }

    // 대상 경로와 요청 URI를 결합하여 대상 URL을 생성합니다.
    // 요청에 쿼리 문자열도 포함되어 있는 경우에도 대응합니다.
    private String createTargetUrl(HttpServletRequest req) {
        String targetUrl = targetPath + req.getRequestURI();
        String queryString = req.getQueryString();
        if (queryString != null) {
            targetUrl += "?" + queryString;
        }
        return targetUrl;
    }

    // 원본 요청 헤더를 프록시 요청에 복사합니다.
    private void copyRequestHeaders(HttpServletRequest req, HttpURLConnection con) throws IOException {
//        req.getHeaderNames().asIterator().forEachRemaining(headerName -> {
//            String headerValue = req.getHeader(headerName);
//            con.setRequestProperty(headerName, headerValue);
//        });
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = req.getHeader(headerName);
            con.addRequestProperty(headerName, headerValue);
        }
    }

    // 원본 요청 파라미터를 프록시 요청에 복사합니다.
    private void copyRequestParameters(HttpServletRequest req, HttpURLConnection con) throws IOException {
//        req.getParameterMap().forEach((paramName, paramValues) -> {
//            for (String paramValue : paramValues) {
//                con.addRequestProperty(paramName, paramValue);
//            }
//        });
        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            for (String paramValue : paramValues) {
                con.addRequestProperty(paramName, paramValue);
            }
        }
    }

    // 프록시 요청 바디를 원본 요청에 복사합니다.
    private void copyRequestBody(HttpServletRequest req, HttpURLConnection con) throws IOException {
        int contentLength = req.getContentLength();
        if (contentLength > 0) {
            con.setDoOutput(true);
            try (OutputStream outputStream = con.getOutputStream();
                 InputStream inputStream = req.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    // 프록시 응답 헤더를 원본 응답에 복사합니다.
    private void copyResponseHeaders(HttpURLConnection con, HttpServletResponse resp) throws IOException {
//        con.getHeaderFields().forEach((headerName, headerValues) -> {
//            if (headerName != null) {
//                for (String headerValue : headerValues) {
//                    resp.addHeader(headerName, headerValue);
//                }
//            }
//        });
        Map<String, List<String>> headerFields = con.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            String headerName = entry.getKey();
            if (headerName != null) {
                List<String> headerValues = entry.getValue();
                for (String headerValue : headerValues) {
                    resp.addHeader(headerName, headerValue);
                }
            }
        }
    }

    // 프록시 응답 파라미터를 원본 응답에 복사합니다.
    private void copyResponseParameters(HttpURLConnection con, HttpServletResponse resp) throws IOException {
//        con.getHeaderFields().forEach((headerName, headerValues) -> {
//            if (headerName != null) {
//                for (String headerValue : headerValues) {
//                    resp.addHeader(headerName, headerValue);
//                }
//            }
//        });
        Map<String, List<String>> headerFields = con.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            String headerName = entry.getKey();
            if (headerName != null) {
                List<String> headerValues = entry.getValue();
                for (String headerValue : headerValues) {
                    resp.addHeader(headerName, headerValue);
                }
            }
        }
    }

    // 프록시 응답 바디를 원본 응답에 복사합니다.
    private void copyResponseBody(HttpURLConnection con, HttpServletResponse resp) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(con.getInputStream());
            outputStream = new BufferedOutputStream(resp.getOutputStream());
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

//        try (InputStream inputStream = con.getInputStream();
//             OutputStream outputStream = resp.getOutputStream()) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        }
    }
}
