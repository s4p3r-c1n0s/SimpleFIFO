package com.mezi.simplefifothread;

import java.util.concurrent.ThreadFactory;

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
