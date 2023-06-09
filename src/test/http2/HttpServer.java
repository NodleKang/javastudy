package test.http2;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

// jetty 웹서버를 생성하고 실행하는 클래스
public class HttpServer extends Thread {

    private static volatile HttpServer instance = null;
    private Server server;
    private int port;

    private HttpServer(int port) {
        this.port = port;
    }

    // 싱글톤 패턴을 따르며, 처음 호출될 때만 HttpServer 인스턴스를 생성하고 스레드로 실행합니다.
    public static HttpServer getInstance(int port) {
        if (instance == null) {
            synchronized (HttpServer.class) {
                instance = new HttpServer(port);
                instance.setName("HttpServer");
                instance.setDaemon(true);
                instance.start();
            }
        }
        return instance;
    }

    // 서버를 구성하고 시작합니다.
    @Override
    public void run() {

        // 서버의 스레드풀 크기 설정
        int maxThreads = 100;
        int minThreads = 2;
        server = new Server(new QueuedThreadPool(maxThreads, minThreads));

        // 서버의 포트 설정
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new ServerConnector[] { connector });

        // 루투 컨텍스트 경로를 "/"로 설정
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

//        context.addServlet(MyServlet.class, "/");
//        context.addServlet(new ServletHolder(new MyServlet()), "/test");
//        context.addServlet(new ServletHolder(new MyServlet()), "/test");

        // 핸들러 설정
        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(context);
        server.setHandler(handlers);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 서버를 중지합니다.
    public void stopServer() {
        try {
            if (instance != null) {
                instance.interrupt();
                instance = null;
                System.out.println("HttpServer stopped.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
