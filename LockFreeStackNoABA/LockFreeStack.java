package Question2;

import java.util.EmptyStackException;
//import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeStack<T> {
  /*public class Node {
    public T value;
    public Node next;
    
    public Node(T value) {
      this.value = value;
      this.next  = null;
    }
  }*/

  private AtomicStampedReference<Node<T>> top = new AtomicStampedReference<Node<T>>(null, 0);
  //static final int MIN_DELAY = ...;
  //static final int MAX_DELAY = ...;
  //Backoff backoff = new Backoff(MIN_DELAY,MAX_DELAY);
  /*
  ThreadLocal<Node> freeList = new ThreadLocal<Node>() {
    protected Node initialValue() { return null; };
  };
  
  private Node alloc(T value) {
    Node node = freeList.get();
    if (node == null) {
      node = new Node(value);
    } else {
      freeList.set(node.next);
      node.value = value;
    }
    return node;
  }

  private void free(Node node) {
    node.next = freeList.get();
    freeList.set(node);
  }*/
  
  protected boolean tryPush(Node<T> node) {
    int[] stamp = new int[1];
    Node<T> oldTop = top.get(stamp);
    node.next = oldTop;
    return top.compareAndSet(oldTop, node, stamp[0], stamp[0]+1);
  }
  
  public void push(Node<T> node) throws InterruptedException {
    //Node node = alloc(value);
    while (true){
      if (tryPush(node)) {
        return;
      } else {
        //backoff.backoff();
      }
    }
  }
  
  protected Node<T> tryPop() throws EmptyStackException {
    int[] stamp = new int[1];
    Node<T> oldTop = top.get(stamp);
    if (oldTop == null){
      throw new EmptyStackException();
    }
    Node<T> newTop = oldTop.next;
    if (top.compareAndSet(oldTop, newTop, stamp[0], stamp[0]+1)) {
      return oldTop;
    } else {
      return null;
    }	
  }
  
  public Node<T> pop() throws EmptyStackException, InterruptedException {
    while (true) {
      Node<T> returnNode = tryPop();
      if (returnNode != null){
        //free(returnNode);
        return returnNode;
      } else {
        //backoff.backoff();
      }
    }
  }
}
