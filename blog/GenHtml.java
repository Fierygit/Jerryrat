package blog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/7 13:31
 */

public class GenHtml {
    private static String head = "";
    private static String head1 = "";
    private static String leftjs = "";
    private static String indexBody = "";


    GenHtml() {
        String path = GenHtml.class.getClassLoader().getResource("").getPath();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "blog/source/head.html"));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                head += temp;
            }
            br.close();
            br = new BufferedReader(new FileReader(path + "blog/source/leftjs.js"));
            while ((temp = br.readLine()) != null) {
                leftjs += temp;
            }
            br.close();

            br = new BufferedReader(new FileReader(path + "blog/source/head1.html"));

            while ((temp = br.readLine()) != null) {
                head1 += temp;
            }
            br.close();

            br = new BufferedReader(new FileReader(path + "blog/post/index.html"));
            while ((temp = br.readLine()) != null) {
                indexBody += temp;
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String getIndexBody() {
        Matcher matcher = Pattern.compile("<body.*?>").matcher(indexBody);
        if (matcher.find() && matcher.end() < indexBody.length()) {
            final String substring = indexBody.substring(matcher.end(), indexBody.length());
            return indexBody;
        }
        return null;
    }

    public static void main(String[] args) {
        new GenHtml();
        GenHtml.getIndexBody();
    }

    public static String getHead() {
        return head;
    }

    public static String getLeftjs() {
        return leftjs;
    }

    public static String getHead1() {
        return head1;
    }
}
