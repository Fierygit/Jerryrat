package core;

import utils.MyLogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/6 13:20
 */

public class Request {
    private String method; //
    private String url; //
    private Map<String, List<String>> parameterMapValues = new HashMap<>(); //
    private InputStream is;
    private String requestInfo = "";
    private boolean isValue = false;
    static String CRLF = "\r\n";

    public Request(InputStream is) {
        this.is = is;
        parseInfo();
    }

    private void parseInfo() {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
        for (int i = 0; i < 8; i++) {
            String temp = null;
            try {
                if ((temp = br.readLine()) != null) {
                    requestInfo += temp + CRLF;
                    // System.out.println(temp);
                } else break;
                if (i == 7) {
                    this.isValue = true;
                    MyLogger.log("INFO", "get over and close the output!");
                }
            } catch (Exception e) {
                MyLogger.log("Exception", "In GetInfoStream: " + e.toString());
            }
        }
        MyLogger.log("INFO", "no input from client");

        if (isValue) {
            String paramString = ""; //
            String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
            int idx = requestInfo.indexOf("/"); //
            this.method = firstLine.substring(0, idx).trim(); //
            String urlStr = firstLine.substring(idx, firstLine.indexOf("HTTP/")).trim();
            if (this.method.equalsIgnoreCase("post")) {
                this.url = urlStr;
                paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
            } else if (this.method.equalsIgnoreCase("get")) {
                if (urlStr.contains("?")) {
                    String[] urlArray = urlStr.split("\\?");
                    this.url = urlArray[0];
                    paramString = urlArray[1];//
                } else {
                    this.url = urlStr;
                }
            }
            // https://www.cnblogs.com/ts.aspx?postId=5798607&pageIndex=0&anchorCommentId=0&_=1581143815767
            if (paramString.equals("")) {
                return;
            }
            parseParams(paramString);
        }
    }


    // uname=aaa&pwd=sss
    private void parseParams(String paramString) {
        StringTokenizer token = new StringTokenizer(paramString, "&");
        while (token.hasMoreTokens()) {
            String keyValue = token.nextToken();
            String[] keyValues = keyValue.split("=");
            if (keyValues.length == 1) {
                keyValues = Arrays.copyOf(keyValues, 2);
                keyValues[1] = null;
            }

            String key = keyValues[0].trim();
            String value = null == keyValues[1] ? null : decode(keyValues[1].trim(), "gbk");
            //
            if (!parameterMapValues.containsKey(key)) {
                parameterMapValues.put(key, new ArrayList<>());
            }
            // 获取引用
            List<String> values = parameterMapValues.get(key);
            values.add(value);
            // 其它 http 信息不管了
        }

    }

    private String decode(String value, String code) {
        try {
            return java.net.URLDecoder.decode(value, code);
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        }
        return null;
    }

    public String[] getParameterValues(String name) {
        List<String> values;
        if ((values = parameterMapValues.get(name)) == null) {
            return null;
        } else {
            return values.toArray(new String[0]);
        }
    }

    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (null == values) {
            return null;
        }
        return values[0];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public Map<String, List<String>> getParameterMapValues() {
        return parameterMapValues;
    }

    public boolean isValue() {
        return isValue;
    }
}
