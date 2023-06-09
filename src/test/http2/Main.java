package test.http2;

public class Main {

    public static void main(String[] args) {
        int port = 8080;
        HttpServer server = HttpServer.getInstance(port); // 내부에 server.start()가 있음
        server.stopServer();
    }
}
