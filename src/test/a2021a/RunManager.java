package test.a2021a;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import test.util.*;

public class RunManager {

    public static void main(String[] args) {
        testOnHttp();
    }

    // 콘솔 입력 기반으로 테스트
    public static void testOnConsole() {

        Scanner scanner = new Scanner(System.in);
        ConcurrentHashMap<String, MessageQueue> mqMap = new ConcurrentHashMap<String, MessageQueue>();

        while (true) {
            // read line from console util user input "exit"
            String line = scanner.nextLine();
            if (line.equals("exit")) {
                break;
            }

            LinkedList<String> commands = MyString.splitToLinkedList(line, " ");

            if (commands.size() > 1) {
                String command = commands.get(0);
                String queueName = commands.get(1);

                if (command.equals("CREATE")) {
                    int capacity = Integer.parseInt(commands.get(2));
                    if (mqMap.containsKey(queueName)) {
                        System.out.println("Queue already exists");
                    } else {
                        MessageQueue<String> mq = new MessageQueue(capacity);
                        mqMap.put(queueName, mq);
                    }
                } else if (command.equals("SEND")) {
                    MessageQueue<String> mq = mqMap.get(queueName);
                    if (mq == null) {
                        System.out.println("Queue does not exist");
                    } else if (mq.size() >= mq.getCapacity()) {
                        System.out.println("Queue is full");
                    } else {
                        mq.addMessage(commands.get(2));
                    }
                } else if (command.equals("RECEIVE")) {
                    MessageQueue<String> mq = mqMap.get(queueName);
                    if (mq == null) {
                        System.out.println("Queue does not exist");
                    } else {
                        String message = mq.getMessage();
                        System.out.println(message);
                    }
                }
            }

        }

    }

    public static void testOnHttp() {
        int port = 8080;
        MyServer server = MyServer.getInstance(port);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
