package test;

import org.eclipse.jetty.server.Server;

public class MyHttpServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new MyHttpHandler());
        server.start();
        server.join();
    }
}
