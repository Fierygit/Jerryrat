package blog;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/8 16:46
 */

public class BlogLogger {

    private String info = "";

    public void log(String info) {
        this.info += (info + "\r\n");
    }

    public String getInfo() {
        return info;
    }


}
