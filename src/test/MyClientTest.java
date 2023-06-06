package test;

import test.http.MyClient;

import java.util.HashMap;
import java.util.Map;

public class MyClientTest {
    public static void main(String[] args) {
        try {
            MyClient httpClient = new MyClient(5000, 5000);

            // GET 요청 테스트
            String getUrl = "https://jsonplaceholder.typicode.com/posts/1";
            String getResponse = httpClient.get(getUrl);
            System.out.println("GET Response:\n" + getResponse);

            // POST 요청 테스트
            String postUrl = "https://jsonplaceholder.typicode.com/posts";
            String postBody = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";
            String postResponse = httpClient.post(postUrl, postBody);
            System.out.println("POST Response:\n" + postResponse);

            // PUT 요청 테스트
            String putUrl = "https://jsonplaceholder.typicode.com/posts/1";
            String putBody = "{\"id\":1,\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";
            String putResponse = httpClient.put(putUrl, putBody);
            System.out.println("PUT Response:\n" + putResponse);

            // DELETE 요청 테스트
            String deleteUrl = "https://jsonplaceholder.typicode.com/posts/1";
            String deleteResponse = httpClient.delete(deleteUrl);
            System.out.println("DELETE Response:\n" + deleteResponse);

            // 요청 헤더 및 속성 설정 테스트를 위한 변수 선언
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer token123");
            Map<String, String> properties = new HashMap<>();
            properties.put("param1", "value1");
            properties.put("param2", "value2");

            // GET 요청 헤더 및 속성 설정 테스트
            String customGetResponse = httpClient.get(getUrl, headers, properties);
            System.out.println("Custom GET Response:\n" + customGetResponse);

            // POST 요청 헤더 및 속성 설정 테스트
            String customPostResponse = httpClient.post(postUrl, postBody, headers, properties);
            System.out.println("Custom POST Response:\n" + customPostResponse);

            // PUT 요청 헤더 및 속성 설정 테스트
            String customPutResponse = httpClient.put(putUrl, putBody, headers, properties);
            System.out.println("Custom PUT Response:\n" + customPutResponse);

            // DELETE 요청 헤더 및 속성 설정 테스트
            String customDeleteResponse = httpClient.delete(deleteUrl, headers, properties);
            System.out.println("Custom DELETE Response:\n" + customDeleteResponse);

            // HttpClient 종료
            httpClient.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
