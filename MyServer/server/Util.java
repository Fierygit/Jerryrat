package MyServer.server;

import java.io.*;
import java.net.URL;

/**
 * @author Firefly
 * @version 1.0
 * @date 2019/11/22 20:16
 */

public class Util {


    /**
     * 如果找不到文件，会报错，返回空
     * @param path
     * @return
     */
    public static String getStatic(String path){
        String content = "";
        try{
            InputStream is = Util.class.getClassLoader().getResourceAsStream("MyServer/WebContent/" + path);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String temp;
            while((temp = br.readLine()) != null){
                //System.out.println(temp);
                //这里可以不加\n
                content += (temp+"\n");
            }
        }catch (Exception e){
            System.out.println("Util: can not find html!!!  path: " + path + "--------------");
            System.out.println(e);
        }
        return content;
    }


    public static boolean primeStatic(String path){
        boolean flag = false;
        try{
            URL resource = Util.class.getClassLoader().getResource("MyServer/WebContent/" + path);
            if(resource != null){
                System.out.println("Util: get resource-->" + resource);
                flag = true;
            }
        }catch (Exception e){
            System.out.println("Util: can not find static!!!  path: " + path + "--------------");
            System.out.println(e);
        }
        return flag;
    }

}
