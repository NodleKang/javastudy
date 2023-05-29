package test;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;

public class MyHttpClient {
    private final HttpClient httpClient;

    public MyHttpClient() throws Exception {
        httpClient = new HttpClient();
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
