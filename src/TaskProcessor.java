import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;


public class TaskProcessor<V> {
	
	private static TaskProcessor _instance;
	
	private static final int CORE_POOL_SIZE = 1;

	private static final int BLOCKING_QUEUE_SIZE = 10;
	
	private static final int KEEP_ALIVE_2_MINS = 2 * 60 * 60 * 1000; 
	
	//private final Worker<Integer> worker ;
	private final ThreadFactory threadFactory ;
	
	private final BlockingQueue<Runnable>workQueue;
	private TaskProcessor()
	{
		threadFactory = new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "TaskThread");
			}
		};
		
		workQueue = new LinkedBlockingQueue<Runnable>(BLOCKING_QUEUE_SIZE);
		
	}
	
	@SuppressWarnings("rawtypes")
	public static TaskProcessor getInstance() 
	{
		if (_instance == null) 
		{
			synchronized (TaskProcessor.class) 
			{
				if (_instance == null) 
				{
					_instance = new TaskProcessor();
				}
			}
		}
		return _instance;
	}
	
	Worker<Integer> worker;
	public Future<V> submit(Callable<V> task)
	{
		if(task == null)
			throw new IllegalArgumentException();
		RunnableFuture<V> fTask = new FutureTask<V>(task);
		execute(fTask);
		return fTask;
	}
	
	private void execute(Runnable command)
	{
		
	}
	
	
	
	

}
