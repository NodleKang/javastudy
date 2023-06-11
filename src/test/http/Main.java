package test.http;

public class Main {

    public static void main(String[] args) {
        int port = 8080;
        MyServer server = MyServer.getInstance(port); // 내부에 server.start()가 있음
        server.stopServer();
    }
}
