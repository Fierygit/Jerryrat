package MyServer.server;

import MyServer.Servlet.HandleStatic;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Administrator
 */
public class Dispatcher implements Runnable {
    private Socket client;
    private Request req;
    private Response rep;
    private String type;
    private int code = 200;

    Dispatcher(Socket client) {
        this.client = client;
        type = "none";
        try {
            System.out.println("a client is trying to connect!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            req = new Request(client.getInputStream());
            String url = req.getUrl();
            int urlLen = url.length();


            if (urlLen > 6) {
                if (url.substring(urlLen - 4, urlLen).contentEquals("html") && url.lastIndexOf(".") == urlLen - 5) {
                    rep = new Response(client.getOutputStream(), "html");
                    type = "html";
                    System.out.println("Dispatcher:init response html");
                }
            }

            if (urlLen > 5) {
                if (url.substring(urlLen - 3, urlLen).contentEquals("css") && url.lastIndexOf(".") == urlLen - 4) {
                    rep = new Response(client.getOutputStream(), "css");
                    type = "css";
                    System.out.println("Dispatcher:init response css");
                }else if (url.substring(urlLen - 3, urlLen).contentEquals("ico") && url.lastIndexOf(".") == urlLen - 4) {
                    rep = new Response(client.getOutputStream(), "ico");
                    type = "css";
                    System.out.println("Dispatcher:init response ico");
                }
            }

            if (urlLen > 4) {
                if (url.substring(urlLen - 2, urlLen).contentEquals("js") && url.lastIndexOf(".") == urlLen - 3) {
                    rep = new Response(client.getOutputStream(), "js");
                    type = "js";
                    System.out.println("Dispatcher:init response js");
                }
            }

            if(type.contentEquals("none"))
            rep = new Response(client.getOutputStream(), "none");


        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("error456");
            code = 500;
        }
    }


    @Override
    public void run() {
        System.out.println("Dispatcher: url------------------");
        System.out.println(req.getUrl());
        // System.out.println(req.getUrl().substring(req.getUrl().length() - 3, req.getUrl().length()));
        if (req.getUrl() == "" || req.getUrl().trim() == "") {
            return;
        }

        /**
         * @author Firefly
         * 如果是静态文件。直接返回
         *
         * html， css， js
         *
         * 静态文件的定义， 含有 .html .js .css 后缀的文件
         *
         */
        if (type.contentEquals("css") || type.contentEquals("html") || type.contentEquals("js")) {

            try {
                HandleStatic servlet = (HandleStatic) Class.forName("MyServer.Servlet.HandleStatic").newInstance();
                if (servlet == null) {
                    System.out.println("Dispatcher: get static servlet error!!!");
                    this.code = 404;
                } else {
                    System.out.println("Dispatcher: this is server for static");
                    /**
                     * @author Firefly
                     * 想找一找有没有这个文件，如果没有的话，返回404
                     */
                    if(Util.primeStatic(req.getUrl())){
                        servlet.setPath(req.getUrl());
                        servlet.service(req, rep);
                    }else {
                        System.out.println("Dispacher: cannt get the static!! "+req.getUrl());
                        this.code = 404;
                    }
                }
                rep.pushToClient(code);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        /**
         * @author Firefly
         * 如果不是静态文件， 目前只支持servlet， 找不到就是服务器内部错误！！！
         */
        else {

            try {
                Servlet serv = WebApp.getServlet(req.getUrl());
                if (null == serv) {
                    this.code = 404;
                } else {
                    serv.service(req, rep);
                }
                rep.pushToClient(code);   //在这里发送数据，

            } catch (Exception e) {
                System.out.println(e);
                System.out.println("error dispatcher");
                //e.printStackTrace();
                this.code = 500;
            }

        }
//
//        try {
//            rep.pushToClient(500);
//        } catch (IOException e) {
//            System.out.println("it is error!!");
//            //e.printStackTrace();
//        }
        req.close();
        rep.close();
        try {
            this.client.close();
            System.out.println("Dispacher: reacte over------------------------------------------------------------------\n\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
