package core;

import utils.MyLogger;
import server.Servlet;
import server.WebApp;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/6 14:31
 */

public class Dispatcher {

    private Request req;
    private Response res;
    private OutputStream os;

    static List<String> staticFile;

    static {
        staticFile = new ArrayList<>();
        staticFile.add("html");
        staticFile.add("ico");
        staticFile.add("css");
        staticFile.add("jpg");
        staticFile.add("js");
        staticFile.add("png");
    }


    Dispatcher(Request req, OutputStream os) {
        this.req = req;
        this.os = os; // 为 response 做准备
    }

    public void dispatch() {
        boolean isStatic = false;
        for (String s : staticFile) {
            if (req.getUrl().endsWith("." + s)) {
                this.res = new Response(this.os, s);
                isStatic = true;
                break;
            }
        }
        if (isStatic) {
            handleStatic();
        } else {
            this.res = new Response(this.os, "html");
            handleServlet();
        }

    }

    private void handleStatic() {
        res.responseByte(req.getUrl());
    }

    private void handleServlet() {

        MyLogger.log("Info", "start run a servlet: " + req.getUrl());

        /**
         * @author Firefly
         * 反射创建  servlet
         */
        Servlet servlet = null;
        try {
            servlet = WebApp.getServlet(req.getUrl());
        } catch (Exception e) {
            MyLogger.log("Exception", "no this servlet");
            res.responseByte("noserverror.html");
            return;  //不能删死循环 后面
        }
        try {
            servlet.service(req, res); // 空的 话上面会捕获
            res.push2clent();
        } catch (Exception e) {
            MyLogger.log("Exception", "no this servlet");
            res.responseByte("500.html");// servlet 运行出错在这里
        }


    }


}
