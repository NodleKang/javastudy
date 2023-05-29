package test;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class MyHttpClient {
    private final HttpClient httpClient;

    public MyHttpClient(int connectionTimeoutMs, int idleTimeoutMs) throws Exception {

        HttpClient httpClientTemp;

        // SSL 컨텍스트 팩토리를 설정하지 않는 경우에는 아래와 같이 설정합니다.
        // httpClientTemp = new HttpClient();

        // SSL 컨텍스트 팩토리를 설정한 httpClientTemp를 사용하려면 아래와 같이 설정합니다.
        // SSL 컨텍스트 팩토리는 SSL/TLS 연결을 사용하는 경우에만 설정합니다.
        httpClientTemp = new HttpClient(new SslContextFactory.Client());

        // 트랜스포트 설정한 httpClientTemp를 사용하려면 아래와 같이 설정합니다.
        // HTTP 트랜스포트는 HTTP, HTTPS, HTTP/2 등을 설정할 수 있습니다.
        // HTTP 트랜스포트를 설정하지 않으면 기본값으로 HTTP/1.1이 설정됩니다.
        // httpClientTemp = new HttpClient(new HttpClientTransportOverHTTP());

        // HttpClient의 스레드 풀을 설정합니다.
        // 스레드 풀의 크기는 동시에 처리할 수 있는 요청의 개수를 의미합니다.
        // 스레드 풀의 크기를 설정하지 않으면 기본값으로 200이 설정됩니다.
        httpClient = httpClientTemp;
        httpClient.setExecutor(new QueuedThreadPool(100));

        // HttpClient의 트랜스포트를 설정합니다.
        // 트랜스포트는 HTTP, HTTPS, HTTP/2 등을 설정할 수 있습니다.
        // 트랜스포트를 설정하지 않으면 기본값으로 HTTP/1.1이 설정됩니다.
        // httpClient.setTransport(new HttpClientTransportOverHTTP2());

        // 리다아렉트 활성화 여부를 설정합니다. (기본값: true = 활성화)
        httpClient.setFollowRedirects(true);

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
