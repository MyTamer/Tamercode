package org.jikesrvm.scheduler.greenthreads;

import org.jikesrvm.VM;
import org.jikesrvm.scheduler.VM_Thread;
import org.vmmagic.pragma.Uninterruptible;

/**
 * See VM_Proxy
 */
@Uninterruptible
public final class VM_ThreadProxyWaitingQueue extends VM_AbstractThreadQueue {

    /** The end of the list of waiting proxies */
    private VM_ThreadProxy tail;

    /** The head of the list of waiting proxies */
    private VM_ThreadProxy head;

    /**
   * Are any proxies on the queue?
   */
    @Override
    public boolean isEmpty() {
        return (head == null);
    }

    /**
   * Put proxy for this thread on the queue.
   * Since a processor lock is held, the proxy cannot be created here.
   * Instead, it is cached in the proxy field of the thread.
   */
    @Override
    public void enqueue(VM_GreenThread t) {
        enqueue(t.threadProxy);
    }

    /**
   * Add the proxy for a thread to tail of queue.
   */
    public void enqueue(VM_ThreadProxy p) {
        if (head == null) {
            head = p;
        } else {
            tail.setWaitingNext(p);
        }
        tail = p;
    }

    /**
   * Remove thread from head of queue.
   * @return the thread (null --> queue is empty)
   */
    @Override
    public VM_GreenThread dequeue() {
        while (head != null) {
            VM_ThreadProxy p = head;
            head = head.getWaitingNext();
            if (head == null) tail = null;
            VM_GreenThread t = p.unproxy();
            if (t != null) return t;
        }
        return null;
    }

    /**
   * Number of items on queue (an estimate: queue is not locked during the scan).
   */
    @Override
    public int length() {
        int i = 0;
        VM_ThreadProxy p = head;
        while (p != null) {
            i = i + 1;
            p = p.getWaitingNext();
        }
        return i;
    }

    boolean contains(VM_Thread t) {
        VM_ThreadProxy p = head;
        while (p != null) {
            if (p.getPatron() == t) return true;
            p = p.getWaitingNext();
        }
        return false;
    }

    void dump() {
        boolean pastFirst = false;
        for (VM_ThreadProxy p = head; p != null; p = p.getWaitingNext()) {
            if (pastFirst) {
                VM.sysWrite(" ");
            }
            if (p.getPatron() != null) {
                p.getPatron().dump();
                pastFirst = true;
            }
        }
        VM.sysWrite("\n");
    }
}
