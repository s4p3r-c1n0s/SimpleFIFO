import java.util.concurrent.Callable;


public class WorkerThread<V> implements Callable<V>{

	private void task() throws InterruptedException {
		Thread.sleep(500, 100);
	}

	@Override
	public V call() throws Exception {
		System.out.println("Starting @" + System.currentTimeMillis() + ": " + Thread.currentThread().getName());
		task();
		System.out.println("Ending @" + System.currentTimeMillis() +": " + Thread.currentThread().getName());
		return null;
	}

}
