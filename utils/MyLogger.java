package utils;

import java.io.*;

/**
 * @author HandleStatic
 * @version 1.0
 * @date 2020/2/5 13:35
 * <p>
 * 设计思路， 全部线程共用一个输出， 用锁来保护
 * <p>
 *     有问题， 不同线程的输出可能混在一起？？？  队列解决？
 * <p>
 * logger 的 等级：     info   Execption
 */

public class MyLogger {


    private final String fileName = "logget.txt";
    private static OutputStream os;
    private static BufferedWriter bw;



    public MyLogger() {
        try {
            os = new FileOutputStream(fileName);
            bw = new BufferedWriter(new OutputStreamWriter(os));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void log(String type, String info){

        System.out.println(
               Util.formatLen(30,Thread.currentThread().getName()) + info);

        if (type.contentEquals("INFO123")) {
            try {
                bw.write(info + "\n");
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new MyLogger();
        MyLogger.log("INFO","hello");

    }


}
