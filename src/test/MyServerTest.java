package test;

import test.http.MyServer;

public class MyServerTest {

    public static void main(String[] args) {
        try {
            // 람다 표현식을 사용하여 Runnable 인터페이스의 run 메서드를 정의합니다.
            // 8080 포트에서 MyServer 인스턴스를 생성하고 실행한 뒤,
            // join 메서드를 사용하여 실행이 완료될 때까지 대기합니다.
            Thread server8080Thread = new Thread(() -> {
                try {
                    MyServer server8080 = new MyServer(8080);
                    server8080.start();
                    server8080.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 쓰레드는 start 메서드를 사용하여 시작됩니다.
            server8080Thread.start();

            // Runnable 인터페이스를 구현한 익명 클래스를 생성합니다.
            // run 메서드를 오버라이드하고 5001 포트에서 MyServer 인스턴스를 생성하고 실행한 뒤,
            // join 메서드를 사용하여 실행이 완료될 때까지 대기합니다.
            Thread server5001Thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MyServer server5001 = new MyServer(5001);
                        server5001.start();
                        server5001.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // 쓰레드는 start 메서드를 사용하여 시작됩니다.
            server5001Thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
