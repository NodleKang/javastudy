package test;

import test.http.MyProxyServer;

public class MyProxyServerTest {

    public static void main(String[] args) {
        try {
            // MyProxyServer 인스턴스 생성 및 서버 시작
            MyProxyServer proxyServer1 = new MyProxyServer(5001);
            MyProxyServer proxyServer2 = new MyProxyServer(3001);

            // 각각의 서버를 별도의 스레드로 실행
            Thread serverThread1 = new Thread(() -> {
                try {
                    proxyServer1.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread serverThread2 = new Thread(() -> {
                try {
                    proxyServer2.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            serverThread1.start();
            serverThread2.start();

            // 메인 스레드를 대기시켜 서버가 계속 실행되도록 유지
            serverThread1.join();
            serverThread2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
