import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyConcurrentSkipList<T> {
    Lock lock = new ReentrantLock();

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public MyConcurrentSkipList() {
        head = null;
        tail = null;
        size = 0;
    }

    private class Node<T> {
        T val;
        Node<T> next;
        Lock lock;

        public Node(T value) {
            this.val = value;
            next = null;
            lock = new ReentrantLock();
        }
    }

    public int size() {
        return size;
    }

    public boolean add(T t) {
        if (head == null && tail == null) {
            synchronized (this) {
                head = new Node<>(t);
                tail = head;
                size++;
            }

        } else {
            Node<T> newNode = new Node<>(t);
            synchronized (this) {
                tail.next = newNode;
                tail = newNode;
                size++;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public boolean contains(T obj) {
        Node<T> tmp = head;
        while (tmp != null) {
            if (obj.equals(tmp.val)) {
                return true;
            }
            tmp = tmp.next;
        }
        return false;
    }

    public boolean remove(T value) {
        if (head == null) {
            return false;
        }

        Node<T> tmp = head;
        Node<T> nextTmp = null;

        while (tmp.next != null) {
            try {
                tmp.lock.lock();
                nextTmp = tmp.next;
                nextTmp.lock.lock();

                if (nextTmp.val.equals(value)) {
                    tmp.next = nextTmp.next;
                    synchronized (this) {
                        size--;
                        if (tmp.next == null) {
                            tail = tmp;
                        }
                    }
                    return  true;
                }
            } finally {
                tmp.lock.unlock();
                nextTmp.lock.unlock();
            }

            tmp = tmp.next;
        }

        if (head.val.equals(value)) {
            synchronized (this) {
                size--;
                head = head.next;
            }
            return true;
        }

        return false;
    }



    public T first() {
        T val = head.val;
        synchronized (this) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size--;
        }
        return val;
    }
}
