package nbodies.utils.barrier;

public class ReusableBarrier implements Barrier {

	private final int max;
	private int n;

	public ReusableBarrier(int nParticipants) {
		n = nParticipants;
		max = nParticipants;
	}

	@Override
	public synchronized void hitAndWaitAll() {
		n--;
		if (n == 0) {
			notifyAll();
			n = max;
		} else {
			try {
				wait();
			} catch (InterruptedException ignored) {
			}
		}
	}
}
