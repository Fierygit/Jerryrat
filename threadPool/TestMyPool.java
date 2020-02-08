package threadPool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author HandleStatic
 * @version 1.0
 * @date 2020/2/5 10:59
 */

public class TestMyPool {


    public static void main(String[] args) {



        ThreadPoolExecutor executor= new MyExecutor().getExecutor();

        for(int i = 0; i < 100; i++){
            final int j = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("hello" + j);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public String toString() {
                    return "Task " + j;
                }
            });

        }
    }
}
