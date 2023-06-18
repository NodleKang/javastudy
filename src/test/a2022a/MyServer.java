package test.a2022a;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.HashMap;

// jetty 웹서버를 생성하고 실행하는 클래스
public class MyServer extends Thread {

    private static volatile MyServer instance = null;
    private Server server;
    private int port;
    private HashMap<String, String> proxyMap = new HashMap<>();

    private MyServer(int port, HashMap<String, String> proxyMap) {
        this.port = port;
        this.proxyMap = proxyMap;
    }

    // 싱글톤 패턴을 따르며, 처음 호출될 때만 HttpServer 인스턴스를 생성하고 스레드로 실행합니다.
    public static MyServer getInstance(int port, HashMap<String, String> proxyMap) {
        if (instance == null) {
            synchronized (MyServer.class) {
                instance = new MyServer(port, proxyMap);
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

        /*
        // 루트 컨텍스트 경로를 "/"로 설정
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // 컨텍스트 경로 설정
        // 인스턴스를 생성하여 원하는 설정을 하고, 서블릿으로 등록하는 방식
        for (String pathPrefix : proxyMap.keySet()) {
            ProxyServlet proxyServlet = new ProxyServlet();
            proxyServlet.setTargetPath(proxyMap.get(pathPrefix));
            context.addServlet(new ServletHolder(proxyServlet), pathPrefix+"/*");
        }

        // 핸들러 설정
        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(context);
        server.setHandler(handlers);
        */

        // 루트 컨텍스트 경로를 "/"로 설정
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(ProxyServlet.class, "/*");

        // 컨텍스트 경로 설정
        for (String pathPrefix : proxyMap.keySet()) {
            ProxyServlet proxyServlet = new ProxyServlet();
            proxyServlet.setTargetPath(proxyMap.get(pathPrefix));
            handler.addServletWithMapping(new ServletHolder(proxyServlet), pathPrefix+"/*");
            //ServletHolder servletHolder = handler.addServletWithMapping(proxyServlet, pathPrefix + "/*");
            //servletHolder.setInitParameter("targetPath", proxyMap.get(pathPrefix));
        }

        // 핸들러 설정
        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(handler);
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
