package test;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class MyProxyServer {

    private final int port;
    private final Server server;

    // 생성자
    public MyProxyServer(int port) {
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
        // 서버 핸들러를 여러 개 추가할 수 있는 핸들러 리스트 생성해서 추가
        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(new MyProxyHandler()); // 기본 핸들러
        handlerList.addHandler(createMyProxyHandler2()); // 컨텍스트 핸들러, 특정 경로에만 적용
        // 서버에 핸들러 리스트 설정
        server.setHandler(handlerList);
    }

    private Handler createMyProxyHandler2() {
        // 컨텍스트 핸들러 생성 (특정 경로에만 적용)
        ContextHandler contextHandler = new ContextHandler("/mypath");
        // 컨텍스트 핸들러에 핸들러 설정
        contextHandler.setHandler(new MyProxyHandler2());
        return contextHandler;
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        MyProxyServer proxyServer = new MyProxyServer(8080);
        proxyServer.start();
    }
}
