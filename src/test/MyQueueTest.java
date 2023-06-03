package test;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class MyQueueTest {

        public static void main(String[] args) {

            // 입력 데이터 형식
            // CREATE [큐이름] [capacity]
            // SEND [큐이름] [메시지]
            // RECEIVE [큐이름]
            Scanner sc = new Scanner(System.in);
            String line;
            Map<String, MyQueue> myQueueMap = new ConcurrentHashMap<>();

            while ((line = sc.nextLine()) != null) {
                // 큐 생성
                if (line.startsWith("CREATE ")) {
                    // 큐 이름과 capacity를 입력받아 큐를 생성합니다.
                    String[] cmsgs = line.split(" ");
                    // 큐 이름이 중복되는지 확인합니다.
                    MyQueue queue = myQueueMap.get(cmsgs[1]);
                    // 큐가 이미 존재하면 큐가 이미 존재한다는 메시지를 출력합니다.
                    if (queue != null) {
                        System.out.println("Queue Exist");
                    // 큐가 존재하지 않으면 큐를 생성합니다.
                    } else {
                        myQueueMap.put(cmsgs[1], new MyQueue(Integer.valueOf(cmsgs[2])));
                    }

                // 큐에 메시지를 전송합니다.
                } else if (line.startsWith("SEND ")) {
                    // 큐 이름과 메시지를 입력받아 큐에 메시지를 전송합니다.
                    String[] smsgs = line.split(" ");
                    // 큐 이름이 존재하는지 확인합니다.
                    MyQueue queue = myQueueMap.get(smsgs[1]);
                    // 큐가 존재하고 큐의 capacity가 0보다 크면 큐에 메시지를 전송합니다.
                    if (queue != null && queue.getCapacity() > 0) {
                        queue.addMessage(smsgs[2]);
                    // 큐가 존재하고 큐의 capacity가 0이면 큐가 가득 찼다는 메시지를 출력합니다.
                    } else if (queue != null && queue.getCapacity() == 0) {
                        System.out.println("Queue Full");
                    }

                // 큐에서 메시지를 수신합니다.
                } else if (line.startsWith("RECEIVE ")) {
                    // 큐 이름을 입력받아 큐에서 메시지를 수신합니다.
                    String[] rmsgs = line.split(" ");
                    // 큐 이름이 존재하는지 확인합니다.
                    MyQueue queue = myQueueMap.get(rmsgs[1]);
                    // 큐가 존재하고 큐가 비어있지 않으면 큐에서 메시지를 수신합니다.
                    if (queue != null && !queue.isEmpty()) {
                        System.out.println(queue.getNextMessage());
                    }
                }
            }
        }
}
