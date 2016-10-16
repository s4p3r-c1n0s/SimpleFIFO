
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


public class TestThreadPool {
	public static void main(String[] args) {
		
		List<Future<Integer>> results = new ArrayList<>();
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10, new ThreadFactory() {
			
			private final AtomicInteger mCount = new AtomicInteger(1);
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setName("UmangThread_" + mCount.getAndIncrement());
				return thread;
			}
		});
		for(int count = 0; count <100; count++) {
			Callable<Integer> cal = new WorkerThread<Integer>();
			results.add(service.submit(cal));
			System.out.println("posted @ " + System.currentTimeMillis());
		}
		for(int c = 0 ; c < results.size() ;c++)
		{
			if(c%2==0)
			{
				System.out.println( "Cancelling : " + results.get(c+1).toString() );
				results.get((c+1)%100).cancel(true);
			}
			try {
				System.out.println( "result : " +  results.get(c).get() + ", isDone : " + results.get(c).isDone() + 
						" @ " + System.currentTimeMillis() + ", cancelled : " + results.get(c).isCancelled());
			} catch (InterruptedException | ExecutionException | CancellationException e) {
				e.printStackTrace();
			}
			
			
		}
		service.shutdown();
	}

}
