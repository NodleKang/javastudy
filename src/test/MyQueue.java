package test;

import java.util.concurrent.LinkedBlockingQueue;

// LinkedBlockingQueue를 사용하여 메시지를 저장하는 큐를 구현합니다.
// LinkedBlockingQueue는 내부적으로 ReentrantLock을 사용하여 스레드 안전성을 보장합니다.
public class MyQueue {
    private LinkedBlockingQueue<Object> queue;

    // LinkedBlockingQueue의 생성자는 capacity를 인자로 받습니다.
    public MyQueue(int capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
    }

    // capacity를 지정하지 않으면 Integer.MAX_VALUE로 지정됩니다.
    public MyQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    // capacity를 변경하고 싶을 때 사용합니다.
    public void setCapacity(int capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
    }

    // capacity를 확인하고 싶을 때 사용합니다.
    public int getCapacity() {
        return queue.remainingCapacity();
    }

    // 큐의 크기를 확인하고 싶을 때 사용합니다.
    public int getSize() {
        return queue.size();
    }

    // 큐에 메시지를 추가합니다.
    public void addMessage(Object message) {
        queue.add(message);
    }

    // 큐에서 메시지를 가져옵니다.
    public Object getNextMessage() throws InterruptedException {
        // return queue.take(); // 큐가 비어있으면 스레드가 블록됩니다.
        return queue.poll(); // 큐가 비어있으면 null을 반환합니다.
    }

    // 큐가 비어있는지 확인합니다.
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
