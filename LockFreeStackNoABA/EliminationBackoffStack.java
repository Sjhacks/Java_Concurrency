package Question2;

import java.util.EmptyStackException;
import java.util.concurrent.TimeoutException;

public class EliminationBackoffStack<T> extends LockFreeStack<T> {
	int capacity;
	int duration;
	EliminationArray<T> eliminationArray = new EliminationArray<T>(capacity,duration);
	static ThreadLocal<RangePolicy> policy = new ThreadLocal<RangePolicy>(); 
	protected synchronized RangePolicy initialValue() {
			return new RangePolicy(2);
	}
	
	public EliminationBackoffStack(int cap, int dur){
		this.capacity = cap;
		this.duration = dur;
	}
	
	public void push(Node<T> node) throws InterruptedException {
		RangePolicy rangePolicy = policy.get();
		//Node node = new Node(value);
		while (true) {
			if (tryPush(node)) {
				return;
			} else try {
				T otherValue = eliminationArray.visit
						(node.value, rangePolicy.getRange());
				if (otherValue == null) {
					rangePolicy.recordEliminationSuccess();
					return; // exchanged with pop
				}
			} catch (TimeoutException ex) {
				rangePolicy.recordEliminationTimeout();
			}
		}
	}
	
	public Node<T> pop() throws EmptyStackException, InterruptedException {
		RangePolicy rangePolicy = policy.get();
		while (true) {
			Node<T> returnNode = tryPop();
			if (returnNode != null) {
				return returnNode;
			} else try {
				T otherValue = eliminationArray.visit(null, rangePolicy.getRange());
				if (otherValue != null) {
					rangePolicy.recordEliminationSuccess();
					returnNode = new Node<T>(otherValue);
					return returnNode;
				}
			} catch (TimeoutException ex) {
				rangePolicy.recordEliminationTimeout();
			}
		}
	}
	
	private static class RangePolicy {
		int maxRange;
		int currentRange = 1;

		RangePolicy(int maxRange) {
			this.maxRange = maxRange;
		}

		public void recordEliminationSuccess() {
			if (currentRange < maxRange)
				currentRange++;
		}

		public void recordEliminationTimeout() {
			if (currentRange > 1)
				currentRange--;
		}

		public int getRange() {
			return currentRange;
		}
	}
}