package test;

import test.http.MyServer;

public class MyServerTest {

    public static void main(String[] args) throws Exception {
        MyServer httpServer = new MyServer(8080);
        httpServer.start();
        httpServer.join();
    }

}
