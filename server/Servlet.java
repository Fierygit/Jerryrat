package server;


import core.Request;
import core.Response;

/**
 * 2019.11.2
 */

public abstract class Servlet {

    public void service(Request req, Response res) throws Exception {

        if (req.getMethod().contentEquals("GET")) this.doGet(req, res);
        else
            this.doPost(req, res);
    }

    protected abstract void doGet(Request req, Response res) throws Exception;

    protected abstract void doPost(Request req, Response res) throws Exception;
}
