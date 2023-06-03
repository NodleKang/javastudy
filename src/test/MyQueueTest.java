package test;

import java.util.Scanner;

public class MyQueueTest {

        public static void main(String[] args) {

            Scanner sc = new Scanner(System.in);
            String line;

            MyQueue queue = new MyQueue();
            while ((line = sc.nextLine()) != null) {
                // SEND로 시작하는 입력이 들어오면 큐에 메시지 추가
                if (line.startsWith("SEND ")) {
                    String msg = line.substring(line.indexOf(" ") + 1);
                    queue.addMessage(msg);
                // RECEIVE로 시작하는 입력이 들어오면 큐에서 메시지 가져오기
                } else if (line.equals("RECEIVE")) {
                    if (!queue.isEmpty()) {
                        System.out.println((String) queue.getNextMessage());
                    }
                // CREATE로 시작하는 입력이 들어오면 큐의 capacity 변경
                } else if (line.startsWith("CREATE ")) {
                    String[] cmsgs = line.split(" ");
                    queue.setCapacity(Integer.valueOf(cmsgs[1]));
                }
            }

        }
}
