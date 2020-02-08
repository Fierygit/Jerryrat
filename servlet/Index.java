package servlet;

import core.Request;
import core.Response;
import server.Servlet;

public class Index extends Servlet {

    public Index() {

    }

    @Override
    public void doGet(Request req, Response res) throws Exception {
        //rep.println("success123.....");
        doPost(req,res);

    }

    @Override
    public void doPost(Request req, Response res) throws Exception {
        // TODO Auto-generated method stub

        /**
         * @author HandleStatic
         * 返回welcome
         */
res.responseByte("index.html");
    }

}
