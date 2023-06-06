package test.http;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class MyServer {

    // 서버가 수신 대기할 포트 번호
    private final int port;
    // Jetty 서버 인스턴스로 서버 자체 (스레드풀 100개)
    private final Server server;

    public MyServer(int port) {
        // 서버가 수신 대기할 포트 번호
        this.port = port;
        // Jetty 서버 인스턴스로 서버 자체 (스레드풀 100개)
        server = new Server(new QueuedThreadPool(100));
        // 서버 커넥터 생성
        ServerConnector connector = new ServerConnector(server);
        // 서버 커넥터에 포트 설정
        connector.setPort(port);
        // 서버 커넥터 설정
        server.setConnectors(new Connector[]{connector});
    }

    public void start() throws Exception {
        // jetty 서버 시작
        server.start();
    }

    public void join() throws InterruptedException {
        // 현재 스레드를 차단하고 서버가 종료될 때까지 대기
        server.join();
    }

    public void stop() throws Exception {
        // jetty 서버 종료
        server.stop();
    }

}
