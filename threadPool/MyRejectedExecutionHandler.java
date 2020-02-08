package threadPool;

import utils.Util;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author HandleStatic
 * @version 1.0
 * @date 2020/2/5 10:53
 */

public class MyRejectedExecutionHandler implements RejectedExecutionHandler {


    //记得去是实现 runnable的tostring 方法
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println(Util.addBlankAtStart(100,r + " has been rejected!"));
    }

}
