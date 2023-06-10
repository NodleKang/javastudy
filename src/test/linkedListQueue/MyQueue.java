package test.linkedListQueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyQueue<E> implements Iterable<E>, Cloneable{

    private Element<E> head;
    private Element<E> tail;
    private int size;

    public MyQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /*
     * 큐의 마지막에 요소를 추가합니다.
     *
     * @param value 큐에 추가할 요소
     * @return 큐에 요소가 추가되면 true를 반환합니다.
     */
    public boolean offer(E value) {

        Element<E> newElement = new Element<>(value);

        if (size == 0) {
            head = newElement;
        } else {
            tail.next = newElement;
        }
        size++;
        tail = newElement;
        return true;
    }

    /*
     * 큐의 첫 번째 요소를 반환하고 제거합니다.
     *
     * @return 큐의 첫 번째 요소
     */
    public E poll() {
        if (size == 0) {
            return null;
        }
        E element = head.data;
        head = head.next;
        size--;

        return element;
    }

    /*
     * 큐의 첫 번째 요소를 반환합니다.
     *
     * @return 큐의 첫 번째 요소
     */
    public E peek() {
        if (size == 0) {
            return null;
        }
        return head.data;
    }

    /*
     * 큐의 첫 번째 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 만약 큐가 비어있다면 예외를 던집니다.
     *
     * @return 큐의 첫 번째 요소
     * @throws NoSuchElementException 큐가 비어있을 때
     */
    public E remove() {
        E element = poll();
        if (element == null) {
            throw new NoSuchElementException();
        }
        return element;
    }

    /*
     * 큐의 첫 번째 요소를 반환합니다.
     * 만약 큐가 비어있다면 예외를 던집니다.
     *
     * @return 큐의 첫 번째 요소
     * @throws NoSuchElementException 큐가 비어있을 때
     */
    public E element() {
        E element = peek();
        if (element == null) {
            throw new NoSuchElementException();
        }
        return element;
    }

    /*
     * 큐의 요소 개수를 반환합니다.
     *
     * @return 큐의 요소 개수
     */
    public int size() {
        return size;
    }

    /*
     * 큐가 비어있는지 여부를 반환합니다.
     *
     * @return 큐가 비어있으면 true를 반환합니다.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
     * 큐에 특정 요소가 포함되어 있는지 여부를 반환합니다.
     *
     * @param o 포함 여부를 확인할 요소
     * @return 큐에 특정 요소가 포함되어 있으면 true를 반환합니다.
     */
    public boolean contains(Object o) {

        if (size == 0 || o == null) {
            return false;
        }
        Element<E> element = head;
        while (element != null) {
            if (element.data.equals(o)) {
                return true;
            }
            element = element.next;
        }
        return false;
    }

    /*
     * 큐의 모든 요소를 삭제합니다.
     */
    public void clear() {
        Element<E> element = head;
        while (element != null) {
            Element<E> next = element.next;
            element.data = null;
            element.next = null;
            element = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    /*
     * 큐의 모든 요소를 배열로 반환합니다.
     *
     * @return 큐의 모든 요소를 담은 배열
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        Element<E> element = head;
        int i = 0;
        while (element != null) {
            array[i++] = element.data;
            element = element.next;
        }
        return array;
    }

    /*
     * 큐의 모든 요소를 담은 배열을 반환합니다.
     * 만약 배열의 크기가 큐의 크기보다 작다면 새로운 배열을 생성해서 반환합니다.
     * 만약 배열의 크기가 큐의 크기보다 크다면 큐의 크기만큼만 잘라서 반환합니다.
     *
     * @param a 큐의 모든 요소를 담을 배열
     * @return 큐의 모든 요소를 담은 배열
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        Element<E> element = head;
        int i = 0;
        while (element != null) {
            a[i++] = (T) element.data;
            element = element.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /*
     * 큐의 모든 요소를 출력합니다.
     */
    public void print() {
        Element<E> element = head;
        while (element != null) {
            System.out.print(element.data + " ");
            element = element.next;
        }
        System.out.println();
    }

    /*
     * 큐의 모든 요소를 역순으로 출력합니다.
     */
    public void reversePrint() {
        Element<E> element = tail;
        while (element != null) {
            System.out.print(element.data + " ");
            element = element.prev;
        }
        System.out.println();
    }

    /*
     * 큐를 복제합니다 (deep copy).
     * 큐의 모든 요소를 포함하는 새로운 큐를 생성해서 반환합니다.
     * 큐의 모든 요소는 큐의 요소와 동일한 요소를 가지는 새로운 요소를 생성해서 사용합니다.
     */
    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            MyQueue<E> clone = (MyQueue<E>) super.clone();
            clone.head = null;
            clone.tail = null;
            clone.size = 0;

            Element<E> element = head;
            while (element != null) {
                clone.offer(element.data);
                element = element.next;
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);

        Iter iter = (MyQueue<E>.Iter) this.iterator();
        for (int i = 0; i < a.length; i++) {
            iter.set((E) a[i]);
            iter.next();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<E> {
        private int index = 0;
        private Element<E> prev = null;
        private Element<E> curr = head;
        private Element<E> next = null;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (index >= size) {
                throw new NoSuchElementException();
            }
            prev = curr;
            curr = next;
            next = next.next;
            index++;
            return (E) curr.data;
        }

        public void set(E e) {
            if (curr == null) {
                throw new IllegalStateException();
            }
            curr.data = e;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
