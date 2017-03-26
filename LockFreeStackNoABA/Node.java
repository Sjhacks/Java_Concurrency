package Question2;

//import Question2.LockFreeStack.Node;

public class Node<T> {
    public T value;
    public Node<T> next;
    
    public Node(T value) {
      this.value = value;
      this.next  = null;
    }
  }
