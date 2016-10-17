import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.lang.Integer;

public abstract class Worker<V> implements Callable<V>{
	
	public Callable<V> firstTask;
	public final Thread thread;
	
	public Worker(Callable<V> firstTask, ThreadFactory threadFactory) {
		this.firstTask = firstTask;
		RunnableFuture<V> runnable = new FutureTask<V>(this);
		this.thread = threadFactory.newThread(runnable);
	}
	
	public abstract V runWorker();

	@Override
	public V call() throws Exception {
		return runWorker();
	}

}
