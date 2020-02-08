package servlet;

import blog.DealFile;
import core.Request;
import core.Response;
import server.Servlet;

/**
 * @author Firefly
 * @version 1.0
 * @date 2020/2/8 15:39
 */

public class UpdateBlog extends Servlet {


    @Override
    protected void doGet(Request req, Response res) throws Exception {

        String user = req.getParameter("user");
        String pwd = req.getParameter("pwd");
        System.out.println("user: " + user);
        System.out.println("pwd: " + pwd);
        if (user == null || pwd == null) {
            res.println("请输入正确的参数！");
            return;
        }
        if (user.contentEquals("firefly") && pwd.contentEquals("123")) {
            DealFile df = new DealFile();
            res.println(df.devBlog());
            res.println("success!");
        } else {
            res.println("账号或者密码错误!");
        }

    }

    @Override
    protected void doPost(Request req, Response res) throws Exception {
        doGet(req, res);
    }


}
