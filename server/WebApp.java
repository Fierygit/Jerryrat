package server;

import utils.MyLogger;

import java.net.URL;
import java.util.List;


import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static java.lang.System.exit;

public class WebApp {
    private static ServletContext contxt;

    static {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sax = factory.newSAXParser();
            WebHandler web = new WebHandler();
            MyLogger.log("INFO", "[WebApp]: start read xml"); //
            //hread.currentThread().getContextClassLoader().getResource("web.xml");


            URL resource = WebApp.class.getClassLoader().getResource("WebContent/config/web.xml");
            String filePath = resource.getPath();
            MyLogger.log("INFO", "[WebApp]: the path of xml -> " + filePath);
            if (filePath == null) {
                MyLogger.log("INFO", "[WebApp]: the web.xml  is not finded ");
                exit(1);
            }
            //sax.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"), web);
            sax.parse(filePath, web);

            //存储xml解析出来的结果
            contxt = new ServletContext();
            Map<String, String> servlet = contxt.getServlet();

            // servlet-name servlet-class
            MyLogger.log("INFO", "[Webapp]: entity");// *******************************************

            for (Entity entity : web.getEntityList()) {
                MyLogger.log("INFO", "\t" + entity.getName() + "->" + entity.getClz());// ********************
                servlet.put(entity.getName(), entity.getClz());
            }
            // url-pattern servlet-name
            Map<String, String> mapping = contxt.getMapping();
            MyLogger.log("INFO", "[Webapp]: mapping");// *******************************************

            for (Mapping mapp : web.getMappingList()) {
                List<String> urls = mapp.getUrlPattern();
                for (String url : urls) {
                    MyLogger.log("INFO", "\t" + url + "->" + mapp.getName());// ********************
                    mapping.put(url, mapp.getName());
                }
            }
        } catch (Exception e) {
            MyLogger.log("INFO", "[WebApp error get xml!!! ");
            exit(1);
            MyLogger.log("INFO", e.toString());
        }
    }

    @SuppressWarnings("deprecation")
    public static Servlet getServlet(String url)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if ((null == url) || (url = url.trim()).equals("")) {
            return null;
        }
        MyLogger.log("INFO", "[WebApp]: search in xml");
        MyLogger.log("INFO","\t" + url + "->"+contxt.getMapping().get(url) + "->");
        MyLogger.log("INFO", "\t" + contxt.getServlet().get(contxt.getMapping().get(url))); // ******************


        String name = contxt.getServlet().get(contxt.getMapping().get(url));
//        name = name == null ? "Error" : name;
        // 不在这里处理 空异常，  在dispatcher 中捕获！！
        String path = "servlet" + "." + name;
        Servlet temp = null;
        try {
            MyLogger.log("INFO", "[WebApp]: servlet path -> " + path);
            temp = (Servlet) Class.forName(path).newInstance();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            MyLogger.log("Exception",e.toString());
            MyLogger.log("Exception", "[WebApp]: " + "error xml -> " + name);
        }
        MyLogger.log("INFO", "[WebApp]: servlet name ->  " + name);
        return temp;//
    }
}
