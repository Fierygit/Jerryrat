package core;


import threadPool.MyExecutor;
import utils.MyLogger;
import server.WebApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.System.exit;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/6 10:22
 */

public class Server {

    private ThreadPoolExecutor executor = new MyExecutor().getExecutor();
    private ServerSocket socket;
    private int port = 8888;


    public void start() {
        try {
            new WebApp();//静态初始化
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("start: " + e.toString());
        }
        receive();
    }

    private void receive() {
        MyLogger.log("INFO", "start receive!");
        while (true) {
            try {
                MyLogger.log("INFO", "Wait for new conntion!!!");
                final Socket connection = socket.accept();
                executor.execute(() -> handleRequest(connection));
                System.out.println();
            } catch (Exception e) {
                MyLogger.log("Exception", "receive: " + e.toString());
                exit(1);
            }
        }
    }


    private void handleRequest(Socket con) {

        MyLogger.log("INFO", "addr: " + con.getRemoteSocketAddress() + "try to con");

        try {
            Request request = new Request(con.getInputStream());
            if (request.isValue()) {
                MyLogger.log("INFO", "requestInfo: \t" + request.getMethod());
                MyLogger.log("INFO", "requestInfo: \t" + request.getUrl());
            } else {
                MyLogger.log("INFO", request.getUrl() + " Error request or has been get!");
            }
            /**
             * @author Firefly
             * 测试发现chrome 会同时发送三个相同的请求， 有时候一些请求还不给流一直阻塞在输入那里
             *  如果同一个chrome发来的请求有效并且，占据输出流， 多个请求是共用同一个输出流输入的， 有时候第一次发来的无效
             *  输入输出  注意  当一个请求已经获取而且有效， 直接占据， con socket 不让其它接受了
             *  所以提前处理一下请求， 无效就返回错误界面
             */
            if (request.isValue()) {
//                synchronized (con) {
                    // 根据请求调度，  要返回什么已经把流传进
                    new Dispatcher(request, con.getOutputStream()).dispatch();
                    con.close();
//                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }


}
