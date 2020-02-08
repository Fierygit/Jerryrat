package core;

import utils.MyLogger;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/6 14:32
 */

public class Response {
    private OutputStream out;

    static final String CRLF = "\r\n";
    static final String BLANK = " ";
    private String path;
    private StringBuilder headInfo = new StringBuilder();
    private InputStream forByte;
    private String resType;
    private int resLen = 0;
    private String forStr = "";


    private static final Map<String, String> mapType = new HashMap<>();

    static {
        mapType.put("css", "Content-type:text/css;charset=utf-8");
        mapType.put("js", "application/javascript");
        mapType.put("html", "Content-type:text/html;charset=utf-8");
        mapType.put("jpg", "image/jpeg");
        mapType.put("png", "image/png");
        mapType.put("ico", "image/png");
    }


    public Response(OutputStream out, String resType) {
        this.out = out;
        this.resType = resType;
        path = Response.class.getClassLoader().getResource("").getPath() + "WebContent/";
    }

    public void responseByte(String path) {
        responseByte(path, 200);
    }

    protected synchronized void responseByte(String path, int code) {
        try {
            this.forByte = new FileInputStream(this.path + path);
        } catch (Exception e) {
            //todo 不要删掉啊会无限死循环的！！！
            MyLogger.log("Exception", "file static file error: " + e.toString());
            MyLogger.log("Exception", "warning warning warning  do not delete nofileerror.html!!!!");
            responseByte("nofileerror.html", 200);
            return;// 不能删除 会死循环 后面
        }

        try {
            this.resLen = this.forByte.available();
            createHeadInfo(code);
            byte[] bytes = headInfo.toString().getBytes();
            out.write(bytes);
            int tlen = 0;
            byte[] temp = new byte[1024];
            while ((tlen = forByte.read(temp)) != -1) {
                this.out.write(temp, 0, tlen);
            }
            out.flush();
            forByte.close();
            out.close();
        } catch (Exception e) {
            MyLogger.log("Exception:", "response: " + e);
            MyLogger.log("Exception", "warning warning warning  do not delete nofileerror.html!!!!");
            responseByte("nofileerror.html", 500);
        }
    }


    public Response print(String value) {
        this.forStr += value;
        return this;
    }

    public Response println(String value) {
        this.forStr += value + "\n";
        return this;
    }

    void push2clent() {
        this.resLen = forStr.getBytes().length;
        createHeadInfo(200);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        try {
            bw.write(this.headInfo + this.forStr);
            bw.flush();
            bw.close();
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    private void createHeadInfo(int code) {
        headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
        switch (code) {
            case 200:
                headInfo.append("OK");
                break;
            case 404:
                headInfo.append("NOT FOUND");
                break;
            case 505:
                headInfo.append("SEVER ERROR");
                break;
        }
        headInfo.append(CRLF);
        headInfo.append("HandleStatic Server/0.0.1").append(CRLF);
        headInfo.append("Date:").append(new Date()).append(CRLF);
        //没有的返回什么？
        if (mapType.get(resType) != null)
            headInfo.append(mapType.get(resType)).append(CRLF);
        else headInfo.append(mapType.get("html")).append(CRLF);
        headInfo.append("Content-Length:").append(resLen).append(CRLF);
        headInfo.append(CRLF);
    }

}
