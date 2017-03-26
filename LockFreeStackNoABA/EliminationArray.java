package Question2;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EliminationArray<T> {
	private int duration;
	Exchanger<T>[] exchanger;
	Random random;
	public EliminationArray(int cap, int dur) {
		exchanger = (Exchanger<T>[]) new Exchanger[cap];
		for (int i = 0; i < cap; i++) {
			exchanger[i] = new Exchanger<T>();
		}
		random = new Random();
		this.duration = dur;
	}
	public T visit(T value, int range) throws TimeoutException, InterruptedException {
		int slot = random.nextInt(range);
		return (exchanger[slot].exchange(value, duration,
		TimeUnit.MILLISECONDS));
	}
}