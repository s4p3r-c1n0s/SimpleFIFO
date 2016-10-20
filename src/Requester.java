import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.lang.Integer;

public abstract class Requester implements Runnable{
	
	public Runnable firstRequest;
	public final Thread thread;
	
	public Requester(Runnable firstRequest, ThreadFactory threadFactory) {
		this.firstRequest = firstRequest;
		this.thread = threadFactory.newThread(this);
	}
	
	public abstract void executeRequest(Requester requester);

	@Override
	public void run() {
		executeRequest(this);
	}

}
