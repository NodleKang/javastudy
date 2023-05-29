package test;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class MyHttpClient {
    private final HttpClient httpClient;

    public MyHttpClient(int connectionTimeoutMs, int idleTimeoutMs) throws Exception {
        httpClient = new HttpClient();

        // Connection timeout 설정(단위: ms)
        httpClient.setConnectTimeout(connectionTimeoutMs);

        // 읽기 시간 초과 설정(단위: ms)
        httpClient.setIdleTimeout(idleTimeoutMs);

        // HttpClient를 시작합니다.
        httpClient.start();
    }

    public String get(String url) throws Exception {
        Request request = httpClient.newRequest(url);
        ContentResponse response = request.send();
        String content = response.getContentAsString();
        return content;
    }

    public String post(String url, String body) throws Exception {
        Request request = httpClient.newRequest(url);
        request.method("POST");
        request.content(new StringContentProvider(body));
        ContentResponse response = request.send();
        String content = response.getContentAsString();
        return content;
    }

    public String put(String url, String body) throws Exception {
        Request request = httpClient.newRequest(url);
        request.method("PUT");
        request.content(new StringContentProvider(body));
        ContentResponse response = request.send();
        String content = response.getContentAsString();
        return content;
    }

    public String delete(String url) throws Exception {
        Request request = httpClient.newRequest(url);
        request.method("DELETE");
        ContentResponse response = request.send();
        String content = response.getContentAsString();
        return content;
    }

    public void stop() throws Exception {
        httpClient.stop();
    }
}
