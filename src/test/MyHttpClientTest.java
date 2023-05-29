package test;

public class MyHttpClientTest {
    public static void main(String[] args) {
        try {
            MyHttpClient myHttpClient = new MyHttpClient();

            // GET 요청 테스트
            String url = "http://127.0.0.1:3001";
            String response = myHttpClient.get(url);
            System.out.println("GET Response:\n" + response);

            // POST 요청 테스트
            String postUrl = "http://127.0.0.1:3001";
            String postBody = "param1=value1&param2=value2";
            String postResponse = myHttpClient.post(postUrl, postBody);
            System.out.println("POST Response:\n" + postResponse);

            // PUT 요청 테스트
            String putUrl = "http://127.0.0.1:3001";
            String putBody = "updated data";
            String putResponse = myHttpClient.put(putUrl, putBody);
            System.out.println("PUT Response:\n" + putResponse);

            // DELETE 요청 테스트
            String deleteUrl = "http://127.0.0.1:3001";
            String deleteResponse = myHttpClient.delete(deleteUrl);
            System.out.println("DELETE Response:\n" + deleteResponse);

            // HttpClient 종료
            myHttpClient.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
