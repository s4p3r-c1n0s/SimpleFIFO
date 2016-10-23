package com.mezi.simplefifothread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


public class TaskProcessor<V> {
	
	private static TaskProcessor<?> _instance;

	private volatile int BLOCKING_QUEUE_SIZE;

	private volatile long KEEP_ALIVE_TIME;

	private volatile Requester loopRequester;
	
	//private final Worker<Integer> worker ;
	private final ThreadFactory threadFactory ;
	
	public final BlockingQueue<Runnable> requestQueue;
	
	private TaskProcessor(int blockingQueueSize, long keepAliveTime)
	{
		
		this.KEEP_ALIVE_TIME =  keepAliveTime;
		this.BLOCKING_QUEUE_SIZE = blockingQueueSize;
		this.threadFactory = new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "TaskThread");
			}
		};
		
		this.requestQueue = new LinkedBlockingQueue<Runnable>();
		
	}
	
	@SuppressWarnings("rawtypes")
	public static TaskProcessor getInstance(int blockingQueueSize, long keepAliveTime) 
	{
		if (_instance == null) 
		{
			synchronized (TaskProcessor.class) 
			{
				if (_instance == null) 
				{
					_instance = new TaskProcessor(blockingQueueSize, keepAliveTime);
				}
			}
		}
		return _instance;
	}
	
	public Future<V> queueRequest(Callable<V> task)
	{
		RunnableFuture<V> runnableFuture = new FutureTask<V>(task);
		execute(runnableFuture);
		return runnableFuture;
	}
	
	private void execute(Runnable request)
	{
		if(loopRequester == null)
		{
			synchronized(TaskProcessor.class)
			{
				System.out.println("adding workerThread @" +  Thread.currentThread().getName());
				if(loopRequester == null)
					spawnThread(request);
			}
		}
		System.out.println("offering @" +  Thread.currentThread().getName());
		Boolean offered = requestQueue.offer(request);
		if(!offered)
		{
			System.out.println("offering failed @" +  Thread.currentThread().getName());
			requestQueue.poll(); // remove First 
			execute(request); // add first
		}
		
	}
	
	private boolean spawnThread(Runnable runnable)
	{
		 loopRequester = new Requester(runnable, threadFactory) {

			@Override
			public void executeRequest(Requester requester) {
				Runnable task = requester.firstRequest;
				requester.firstRequest = null;
				while(task != null) {
					System.out.println("task not null @" +  Thread.currentThread().getName());
					try {
						task.run();
					} finally {
						task = null;
						task = fetchMoreRequestFromQueue();
					}
				}
				System.out.println("THREAD COMPLETELY ENDED @" +  Thread.currentThread().getName());
			}
			 
		 };
		 loopRequester.thread.start();
		 return true;
	}
	
	private Runnable fetchMoreRequestFromQueue()
	{
		try {
			System.out.println("polling : " + Thread.currentThread().getName());
			return requestQueue.poll(KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	

}
