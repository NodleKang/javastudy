package test.linkedListQueue;

public class Element<E> {

    E data;
    Element<E> prev;
    Element<E> next;

    public Element(E data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
