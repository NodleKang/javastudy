package test.mq;

import java.util.Iterator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue<V> {

    // 메시지를 저장하는 큐
    private LinkedBlockingQueue<V> queue;
    // 메시지 아이디와 메시지를 매핑하는 맵
    private ConcurrentHashMap<String, V> idMessageMap;
    // 메시지와 메시지 아이디를 매핑하는 맵
    private ConcurrentHashMap<V, String> messageIdMap;
    // 메시지와 메시지 상태를 매핑하는 맵
    private ConcurrentHashMap<V, MessageStatus> messageStatusMap;
    // 메시지와 메시지 타임아웃을 매핑하는 맵
    private ConcurrentHashMap<V, Long> messageTimeoutMap;
    // 메시지와 메시지 실패 횟수를 매핑하는 맵
    private ConcurrentHashMap<V, Integer> messageFailedCountMap;

    // 큐의 용량
    private int capacity = 0;
    // 메시지 실패 횟수의 최대값
    private int MAX_MESSAGE_FAILED_COUNT = 3;

    // 메시지 상태를 나타내는 열거형
    private enum MessageStatus {
        PENDING, IN_PROGRESS, FAILED
    }

    // 생성자(용량을 지정하는 생성자)
    public MessageQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedBlockingQueue<>();
        initializeMaps();
    }

    // 생성자(용량을 지정하지 않는 생성자)
    public MessageQueue() {
        this.queue = new LinkedBlockingQueue<>();
        initializeMaps();
    }

    // 맵을 초기화하는 메서드
    private void initializeMaps() {
        this.idMessageMap = new ConcurrentHashMap<>();
        this.messageIdMap = new ConcurrentHashMap<>();
        this.messageStatusMap = new ConcurrentHashMap<>();
        this.messageTimeoutMap = new ConcurrentHashMap<>();
        this.messageFailedCountMap = new ConcurrentHashMap<>();
    }

    // 큐의 용량을 설정하는 메서드
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // 메시지를 큐에 추가하는 메서드
    public void addMessage(V message) {

        // 큐의 용량이 0보다 크고 큐의 크기가 용량보다 크거나 같으면 예외 발생
        if (capacity > 0 && queue.size() >= capacity) {
            throw new IllegalStateException("Queue is full");
        }
        // 큐에 메시지를 추가하고 메시지와 메시지 아이디를 매핑하는 맵에 메시지와 메시지 아이디를 추가
        if (queue.offer(message)) {
            String id = UUID.randomUUID().toString();
            idMessageMap.put(id, message);
            messageIdMap.put(message, id);
            messageStatusMap.put(message, MessageStatus.PENDING);
            messageTimeoutMap.put(message, 0L);
            messageFailedCountMap.put(message, 0);
        }
    }

    // 큐에서 메시지를 가져오는 메서드
    public synchronized V getMessage() {

        // 메시지 가져오기 로직을 동기화 블록으로 감싸기
        synchronized (this) {

            // 큐에 메시지가 있으면 메시지를 가져옴
            for (V message : queue) {

                MessageStatus messageStatus = messageStatusMap.get(message);

                // 메시지 상태가 PENDING이면 IN_PROGRESS로 변경하고 메시지를 반환
                if (messageStatus == MessageStatus.PENDING) {
                    messageStatusMap.put(message, MessageStatus.IN_PROGRESS);
                    return message;
                } else if (messageStatus == MessageStatus.IN_PROGRESS) {
                    long timeout = messageTimeoutMap.get(message);
                    if (timeout != 0L && System.currentTimeMillis() - timeout > 0) {
                        messageStatusMap.put(message, MessageStatus.FAILED);
                        messageFailedCountMap.put(message, messageFailedCountMap.get(message) + 1);
                        return message;
                    }
                } else if (messageStatus == MessageStatus.FAILED) {
                    if (messageFailedCountMap.get(message) < MAX_MESSAGE_FAILED_COUNT) {
                        messageStatusMap.put(message, MessageStatus.IN_PROGRESS);
                        return message;
                    } else {
                        messageStatusMap.put(message, MessageStatus.PENDING);
                        messageFailedCountMap.put(message, 0);
                        messageTimeoutMap.put(message, System.currentTimeMillis() + 1000);
                    }
                }
            }
        }

        // 큐에 메시지가 없으면 null 반환
        return null;
    }

    // 메시지 아이디를 기반으로 메시지를 큐에서 가져오는 메서드
    public V getMessageById(String id) {
        return idMessageMap.get(id);
    }

    // 메시지 성공시 메시지를 큐에서 제거하는 메서드
    public void handleMessageSuccess(V message) {
        removeMessage(message);
    }

    // 메시지 성공시 메시지 아이디를 기반으로 메시지를 큐에서 제거하는 메서드
    public void handleMessageSuccessById(String id) {
        removeMessage(getMessageById(id));
    }

    // 메시지 실패시 메시지 상태를 PENDING으로 변경하고 메시지 실패 횟수를 증가시키는 메서드
    // 메시지 상태를 PENDING으로 변경하고 메시지 실패 횟수를 증가시키는 메서드
    public void handleMessageFailure(V message) {
        messageStatusMap.put(message, MessageStatus.PENDING);
        messageFailedCountMap.put(message, messageFailedCountMap.get(message) + 1);
    }

    // 메시지 상태가 PENDING인지 확인하는 메서드
    public boolean isMessagePending(V message) {
        return messageStatusMap.get(message).equals(MessageStatus.PENDING);
    }

    // 메시지 상태가 IN_PROGRESS인지 확인하는 메서드
    public boolean isMessageInProgress(V message) {
        return messageStatusMap.get(message).equals(MessageStatus.IN_PROGRESS);
    }

    // 메시지 상태가 FAILED인지 확인하는 메서드
    public boolean isMessageFailed(V message) {
        return messageStatusMap.get(message).equals(MessageStatus.FAILED);
    }

    // 메시지 타임아웃이 지났는지 확인하는 메서드
    public boolean isMessageTimeout(V message) {
        if (messageTimeoutMap.get(message) != 0L) {
            if (System.currentTimeMillis() - messageTimeoutMap.get(message) > 0) {
                return true;
            }
        }
        return false;
    }

    // 메시지 실패 횟수가 최대값을 초과했는지 확인하는 메서드
    public boolean isMessageFailedCountExceeded(V message) {
        if (messageFailedCountMap.get(message) >= MAX_MESSAGE_FAILED_COUNT) {
            return true;
        }
        return false;
    }

    // 메시지를 큐에서 제거하는 메서드
    public void removeMessage(V message) {
        queue.remove(message);
        idMessageMap.remove(messageIdMap.get(message));
        messageIdMap.remove(message);
        messageStatusMap.remove(message);
        messageTimeoutMap.remove(message);
        messageFailedCountMap.remove(message);
    }

    // 메시지 아이디를 기반으로 메시지를 큐에서 제거하는 메서드
    public void removeMessageById(String id) {
        V message = idMessageMap.get(id);
        if (message != null) {
            removeMessage(message);
        }
    }

    // 메시지 상태를 기반으로 메시지를 큐에서 제거하는 메서드
    public void removeMessageByStatus(MessageStatus status) {
        Iterator<V> iterator = queue.iterator();
        while (iterator.hasNext()) {
            V message = iterator.next();
            if (messageStatusMap.get(message).equals(status)) {
                iterator.remove();
                removeMessage(message);
            }
        }
    }

    // 메시지 타임아웃을 기반으로 메시지를 큐에서 제거하는 메서드
    public void removeMessageByTimeout(long timeout) {
        Iterator<V> iterator = queue.iterator();
        while (iterator.hasNext()) {
            V message = iterator.next();
            if (messageTimeoutMap.get(message) != 0L) {
                if (System.currentTimeMillis() - messageTimeoutMap.get(message) > timeout) {
                    iterator.remove();
                    removeMessage(message);
                }
            }
        }
    }

    // 메시지 실패 횟수를 기반으로 메시지를 큐에서 제거하는 메서드
    public void removeMessageByFailedCount(int count) {
        Iterator<V> iterator = queue.iterator();
        while (iterator.hasNext()) {
            V message = iterator.next();
            if (messageFailedCountMap.get(message) > count) {
                iterator.remove();
                removeMessage(message);
            }
        }
    }

}
