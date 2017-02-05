package question2_2;


import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;


public class LockFreeList<T> {
  /**
   * First list node
   */
  private Node head;
  private Node tail;
  /**
   * Constructor
   */
  public LockFreeList(T item1, T item2) {
    this.head  = new Node(item1,Integer.MIN_VALUE);
    this.tail = new Node(item2,2);
    this.tail.next = new AtomicMarkableReference<Node>(head,false);
    while (!head.next.compareAndSet(null, tail, false, false));
  }
  /**
   * Add an element.
   * @param item element to add
   * @return true iff element was not there already
   */
  public boolean add(T item, int pos) {
    int key = pos;
    boolean splice;
    while (true) {
      // find predecessor and curren entries
      Window window = find(head, key);
      Node pred = window.pred, curr = window.curr;
      // is the key present?
      /*if (curr.key == key) {
        return false;
      } else {*/
        // splice in new node
        Node node = new Node(item, key);
        node.next = new AtomicMarkableReference<Node>(curr, false);
        if(pred == tail && curr ==head){
        	tail = node;
        }
        if (pred.next.compareAndSet(curr, node, false, false)) {
          return true;
        }
      //}
    }
  }
  /**
   * Remove an element.
   * @param item element to remove
   * @return true iff element was removed
   */
  public boolean remove(int pos) {
    int key = pos;
    boolean snip;
    while (true) {
      // find predecessor and curren entries
      Window window = find(head, key);
      Node pred = window.pred, curr = window.curr;
      // is the key present?
      if (curr.key != key) {
        return false;
      } else {
        // snip out matching node
        Node succ = curr.next.getReference();
        snip = curr.next.attemptMark(succ, true);
        if (!snip)
          continue;
        if(curr == tail){
        	tail = pred;
        }
        pred.next.compareAndSet(curr, succ, false, false);
        return true;
      }
    }
  }
  /**
   * Test whether element is present
   * @param item element to test
   * @return true iff element is present
   */
  public boolean contains(T item) {
    int key = item.hashCode();
    // find predecessor and curren entries
    Window window = find(head, key);
    Node pred = window.pred, curr = window.curr;
    return (curr.key == key);
  }
  /**
   * list node
   */
  public class Node {
    /**
     * data
     */
    T item;
    /**
     * item placement
     */
    int key;
    /**
     * atomic reference to next node in list
     */
    AtomicMarkableReference<Node> next;
    /**
     * Constructor for usual node
     * @param item element in list
     */
    Node(T item, int key) {      // usual constructor
      this.item = item;
      this.key = key;
      this.next = new AtomicMarkableReference<Node>(null, false);
    }
    /**
     * Constructor for sentinel node
     * @param key should be min or max int value
     */
    Node(int key) { // sentinel constructor
      this.item = null;
      this.key = key;
      this.next = new AtomicMarkableReference<Node>(null, false);
    }
    /**
     * Getter for data
     * @return returns data(item)
     */
    public T getData(){
    	return item;
    }
  }

  /**
   * Pair of adjacent list entries as an object
   */
  class Window {
    /**
     * Preceding node
     */
    public Node pred;
    /**
     * current node.
     */
    public Node curr;
    /**
     * Constructor.
     */
    Window(Node pred, Node curr) {
      this.pred = pred; this.curr = curr;
    }
  }

  /**
   * If element is present, returns node and predecessor. If absent, returns
   * node with least larger key. If the key is bigger than other keys in the list, return tail and head
   * @param head start of list
   * @param key key to search for
   * @return If element is present, returns node and predecessor. If absent, returns
   * node with least larger key.
   */
  public Window find(Node head, int key) {
    Node pred = null, curr = null, succ = null;
    boolean[] marked = {false}; // is curr marked?
    boolean snip;
    retry: while (true) {
      pred = head;
      curr = pred.next.getReference();
      while (true) {
        succ = curr.next.get(marked);
        while (marked[0]) {           // replace curr if marked
          snip = pred.next.compareAndSet(curr, succ, false, false);
          if (!snip) continue retry;
          curr = pred.next.getReference();
          succ = curr.next.get(marked);
        }
        if (curr.key >= key)
          return new Window(pred, curr);
        if(key >tail.key){
        	return new Window(tail,head);
        }
        pred = curr;
        curr = succ;
      }
    }
  }
  /**
   * Getter for head
   * @return head
   */
  public Node getHead(){
	  return this.head;
  }
  /**
   * prints out the contents of the list on a line
   */
  public void display(){
	  Node ptr = this.head;
	  System.out.print("final list: ");
	  while(ptr!=tail){
		  System.out.print(ptr.getData()+" ");
		  ptr = ptr.next.getReference();
	  }
	  System.out.println(tail.getData());
  }
  /**
   * method used for thread0, prints out elements of the list every 100ms
   */
  public void printout(){
	  Node ptr = this.head;
	  while(true){
		  System.out.print(ptr.getData()+ " ");
		  ptr = ptr.next.getReference();
		  try {
			Thread.sleep(100);
		  } catch (InterruptedException e) {
			  System.out.println("thread0 done");
			return;
		  }
	  }
  }
  /**
   * method used for thread1, removes an element of the list with 1/10 chance as it iterates through
   */
  public void remover(){
	  Node ptr1 = this.head;
	  //int position = 0;
	  while(true){
		  if(ptr1 == head){
			  //position =0;
		  }
		  int roll = (int)(Math.random() * (11));
		  //int roll = 5;
		  if(roll == 5){
			  if((char)ptr1.getData() !='A' && (char)ptr1.getData()!='B' && (char)ptr1.getData()!='C'){
				  Node tmp = ptr1.next.getReference();
				  this.remove(ptr1.key);
				  ptr1 = tmp;
				  try {
					Thread.sleep(20);
				  } catch (InterruptedException e) {
					System.out.println("thread1 done");
					return;
				  }
				  continue;
				  //position++;
			  }
		  }
		  ptr1 = ptr1.next.getReference();
		  //position++;
		  try {
			  Thread.sleep(20);
		  } catch (InterruptedException e) {
			  System.out.println("thread1 done");
			  return;
		  }
	  }
  }
  /**
   * method used for thread2, adds an element with 1/10 chance as it iterates throught the list
   */
  public void adder(){
	  Node ptr2 = this.head;
	  int position = 1;
	  Random r = new Random();
	  while(true){
		  if(ptr2 == head){
			  position = 1;
		  }
		  int roll = (int)(Math.random() * (11));
		  if(roll == 5){
			  this.add((T)(Character)(char)(r.nextInt(23) + 'D'), position);
		  }
		  ptr2 = ptr2.next.getReference();
		  position++;
		  try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			System.out.println("thread2 done");
			return;
		}
	  }
  }
}