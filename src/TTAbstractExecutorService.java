import java.util.concurrent.*;
import java.util.*;

public abstract class TTAbstractExecutorService implements Executor {

	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		return new FutureTask<T>(callable);
	}

	public <T> Future<T> submit(Callable<T> task) {
		if (task == null)
			throw new NullPointerException();
		RunnableFuture<T> ftask = newTaskFor(task);
		execute(ftask);
		return ftask;
	}

}
