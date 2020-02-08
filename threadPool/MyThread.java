package threadPool;


import utils.Util;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {

    private static final String DEDAULT_NAME = "MyAppThread";
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();

    public MyThread(Runnable r) {
        this(r, DEDAULT_NAME);
    }

    public MyThread(Runnable r, String name) {
        super(r, name + "-" + created.incrementAndGet());
    }

    public void run() {

        System.out.println(Util.addBlankAtStart(100,"Created " + getName())); // 开始运行时 提示
        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            System.out.println(Util.addBlankAtStart(100,"Exiting " + getName()));//退出时提示
        }
    }



    public static int getCreated() {
        return created.get();
    }

    public static AtomicInteger getAlive() {
        return alive;
    }
}