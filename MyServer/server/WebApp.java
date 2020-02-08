package MyServer.server;

import java.util.List;


import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WebApp {
	private static ServletContext contxt;
	static {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sax = factory.newSAXParser();
			WebHandler web = new WebHandler();
			System.out.println("\nWebApp: start read xml-----------------"); //

			//hread.currentThread().getContextClassLoader().getResource("web.xml");

			System.out.println("WebApp: the path of xml");
			System.out.println(WebApp.class.getClassLoader().getResource("MyServer/WebContent/config/web.xml").getPath());
			//sax.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"), web);
			sax.parse(WebApp.class.getClassLoader().getResourceAsStream("MyServer/WebContent/config/web.xml"), web);



			//存储xml解析出来的结果
			contxt = new ServletContext();
			Map<String, String> servlet = contxt.getServlet();

			// servlet-name servlet-class
			System.out.println("Webapp: entity----------------");// *******************************************

			for (Entity entity : web.getEntityList()) {
				System.out.println(entity.getName() + "->" + entity.getClz());// ********************
				servlet.put(entity.getName(), entity.getClz());
			}

			// url-pattern servlet-name
			Map<String, String> mapping = contxt.getMapping();
			System.out.println("Webapp: mapping----------------");// *******************************************

			for (Mapping mapp : web.getMappingList()) {
				List<String> urls = mapp.getUrlPattern();
				for (String url : urls) {
					System.out.println(url + "->" + mapp.getName());// ********************
					mapping.put(url, mapp.getName());
				}
			}
		} catch (Exception e) {
			System.out.println("WebApp error get xml!!!");
			System.out.println(e);
		}
	}

	@SuppressWarnings("deprecation")
	public static Servlet getServlet(String url)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if ((null == url) || (url = url.trim()).equals("")) {
			return null;
		}
		System.out.println("WebApp: search in xml----------------------------");
		System.out.print(url + "->");
		System.out.print(contxt.getMapping().get(url)+"->");
		System.out.println(contxt.getServlet().get(contxt.getMapping().get(url))); // ******************

		// return contxt.getServlet().get(contxt.getMapping().get(url));
		String name = contxt.getServlet().get(contxt.getMapping().get(url));
		name =  name == null ? "Error" : name;
		String path = "MyServer.Servlet" + "." + name;
		Servlet temp = null;
		try {
			System.out.println("WebApp: " + path);
			temp = (Servlet) Class.forName(path).newInstance();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("WebApp: " +"xml"+name);
		}
		System.out.println("WebApp: servlet name: "+ name + "---------------------");
		return temp;//
	}
}
