package MyServer.Servlet;

import MyServer.server.Request;
import MyServer.server.Response;
import MyServer.server.Servlet;

public class Error extends Servlet {

	public Error() {

	}
	
	@Override
	public void doGet(Request req, Response rep) throws Exception {
		//rep.println("success123.....");
	}

	@Override
	public void doPost(Request req, Response rep) throws Exception {
		// TODO Auto-generated method stub

		/**
		 * @author Firefly
		 * 返回welcome
		 */
		rep.print("error!!!中文");
	}

}
