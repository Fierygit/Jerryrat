package MyServer.Servlet;

import MyServer.server.Request;
import MyServer.server.Response;
import MyServer.server.Servlet;

public class HandleStatic extends Servlet {

	private String path;

	public HandleStatic() {

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

		rep.response(path);
	}

}
