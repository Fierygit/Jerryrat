package threadPool;


import java.util.concurrent.ThreadFactory;

/**
 * @author HandleStatic
 * @version 1.0
 * @date 2020/2/5 10:46
 */

public class MyThreadFactory implements ThreadFactory {
    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    public Thread newThread(Runnable r) {
      return new MyThread(r, poolName);
    }
}
