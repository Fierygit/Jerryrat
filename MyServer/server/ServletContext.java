package MyServer.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 2019.11.22
 * @author Firefly
 *
 */
public class ServletContext {
	//
	// login  --> class
	private Map<String,String> servlet ;

	
	
	//url -->login
	private Map<String,String> mapping;
	
	ServletContext(){
		servlet =new HashMap<String,String>();
		mapping =new HashMap<String,String>();
	}

	public Map<String, String> getServlet() {
		return servlet;
	}
	public void setServlet(Map<String, String> servlet) {
		this.servlet = servlet;
	}
	public Map<String, String> getMapping() {
		return mapping;
	}
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
	
	
	
	
}
