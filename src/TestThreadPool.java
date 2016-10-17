
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class TestThreadPool {
	private static void delay() throws InterruptedException
	{
		System.out.println("start");Thread.sleep(1000); System.out.println("end");
	}
	public static void main(String[] args) {

		
		List<Callable<String>> callables = Arrays.asList(
		        () -> {delay();return "task1";},
		        () -> {delay();return "task2";},
		        () -> {delay();return "task3";});
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		String[] myArray = { "this", "is", "a", "sentence", "which", "says", "that", "I", "am", "a", "gen" };
		String result = Arrays.stream(myArray)
		                .reduce(" - ", (a,b) -> a + " " + b);
		System.out.println("U : "+ result);
		
		try {
			executor.invokeAll(callables)
					.stream()
					.map((future) -> {
							try {
								System.out.println("mapping");
								return future.get();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								throw new  IllegalStateException(e);
							}	
					})
					.forEach(System.out::println);
			System.out.println("mapping Done");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
