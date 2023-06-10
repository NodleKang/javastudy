package test.httpbackup;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class MyServer {

    // 서버가 수신 대기할 포트 번호
    private final int port;
    // Jetty 서버 인스턴스로 서버 자체 (스레드풀 100개)
    private final Server server;

    public MyServer(int port) {
        // 서버가 수신 대기할 포트 번호
        this.port = port;
        // Jetty 서버 인스턴스로 서버 자체 (기본 스레드풀 200개)
        server = new Server();
        //server = new Server(new QueuedThreadPool(100)); // 스레드풀 100개 설정시

        // 서버 커넥터(서버에 연결된 클라이언트와의 통신 담당) 생성
        // Jetty 서버에 들어오는 연결을 관리하고 요청을 처리하는 핵심적인 역할을 수행한다.
        // Jetty 서버에 바인딩되어 들어오는 연결을 수락하고, 해당 연결에 대한 요청을 처리한다.
        // HTTP나 HTTPS와 같은 프로토콜에 따라 들어오는 연결을 처리할 수 있다.
        // 다중 인터페이스를 지원하여 서버의 여러 IP 주소 또는 포트에 바인딩할 수 있다.
        // 포트 번호, 호스트 주소, 백로그 큐 크기, SSL 설정 등의 속성을 지정할 수 있다.
        // 연결을 처리하기 위한 네트워크 I/O 스레드 수 등의 세부적인 설정도 가능하다.
        ServerConnector connector = new ServerConnector(server);
        // 서버 커넥터에 포트 설정
        connector.setPort(port);
        // 서버 커넥터 추가
        server.addConnector(connector);
        // 서버 커넥터 설정
        // server.setConnectors(new Connector[]{connector});

        // 서버 핸들러(서버가 요청을 처리하는 방법을 정의) 생성
        MyRequestHandler myRequestHandler = new MyRequestHandler();
        // 서버 핸들러 추가
        server.setHandler(myRequestHandler);
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
