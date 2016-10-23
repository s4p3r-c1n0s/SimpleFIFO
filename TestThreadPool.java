package com.mezi.simplefifothread;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class TestThreadPool {
	
	
	public static void main(String[] args) {	
		final List<Future<Integer>> totalResults = new  CopyOnWriteArrayList<Future<Integer>>();
		final TaskProcessor<Integer> tp = TaskProcessor.getInstance(3, 10000l);
		for(int i = 0; i<10;i++)
		{
			final int loopValue = i;
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					totalResults.addAll(addDummyTasks(loopValue, 10, tp));
				}
			};
			new Thread(runnable, "dummyTaskThread_" + i).start();
		}
		
	}
	
	private static List<Future<Integer>> addDummyTasks(final int loopCount ,int noOfTasks, TaskProcessor<Integer> tp)
	{
		List<Future<Integer>> results = new CopyOnWriteArrayList<Future<Integer>>();
		for(int i = 0; i <noOfTasks ; i++) {
			final Integer taskCount = i;
			Future<Integer> result = tp.queueRequest(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					System.out.println("Callable: l" +loopCount +" r"+ taskCount + " @ " + Thread.currentThread().getName());
					Thread.sleep(1);
					return taskCount + (100* loopCount);
				}
				
			});
			results.add(result);
			
		}
		
		Iterator<Future<Integer>> itr = results.iterator();
		while( itr.hasNext() )
		{
			try {
				System.out.println(itr.next().get() + " COMPLETED " + Thread.currentThread().getName());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return results;
	}

}
