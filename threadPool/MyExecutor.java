package threadPool;

import java.util.concurrent.*;

/**
 * @author HandleStatic
 * @version 1.0
 * @date 2020/2/5 9:53
 */

public class MyExecutor {

    private ThreadPoolExecutor executor;

    private int corePoolSize = 10;
    private int maximumPoolSize = 12;
    private long keepAliveTime = 8;
    private TimeUnit unit = TimeUnit.SECONDS;

    //有界队列10个
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);

    ThreadFactory threadFactory = new MyThreadFactory("MyPoolThread");

    RejectedExecutionHandler handler = new MyRejectedExecutionHandler();


    public MyExecutor() {

        this.executor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit, workQueue, threadFactory, handler);
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public ThreadPoolExecutor getExecutor() {
        return this.executor;
    }

}
