package MyServer.server;

import com.sun.org.glassfish.gmbal.ParameterNames;

import java.io.BufferedWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 
 * @author Administrator
 *
 */
public class Response {
	
	public static final String CRLF = "\r\n";
	public static final String BLANK = " ";

	private BufferedWriter bw; 
	private StringBuilder content; 
	private StringBuilder headInfo; 
	private String ContentType;
	private int len = 0; 

	public Response() {             
		headInfo = new StringBuilder();
		content = new StringBuilder();
		len = 0;
		ContentType = "html";
	}

	public Response(Socket client) {  
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			headInfo = null;
		}
	}

	public Response(Socket client, String ContentType) {  
		this();
		this.ContentType = ContentType;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			headInfo = null;
		}
	}

	public Response(OutputStream os, String ContentType) {
		this();
		this.ContentType = ContentType;
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}

	/**
	 */
	public Response print(String info) {
		content.append(info);
		len += info.getBytes().length;  
		return this;
	}


	@ParameterNames("默认找WebContent下的html")
	public Response response(String path) {
		String res = Util.getStatic(path);
		content.append(res);
		len += res.getBytes().length;
		return this;
	}


	/**
	 */
	public Response println(String info) {
		content.append(info).append(CRLF);
		len += (info + CRLF).getBytes().length;
		return this;
	}

	/**
	 */
	private void createHeadInfo(int code) {
		headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
		switch (code) {
		case 200:
			headInfo.append("OK");
			break;
		case 404:
			headInfo.append("NOT FOUND");
			break;
		case 505:
			headInfo.append("SEVER ERROR");
			break;
		}
		headInfo.append(CRLF);
		headInfo.append("Firefly Server/0.0.1").append(CRLF);
		headInfo.append("Date:").append(new Date()).append(CRLF);
		 if(ContentType.equals("css")){
			headInfo.append("Content-type:text/css;charset=utf-8").append(CRLF);
		}else{
			headInfo.append("Content-type:text/html;charset=utf-8").append(CRLF);
		}

		
		headInfo.append("Content-Length:").append(len).append(CRLF);
		headInfo.append(CRLF); 
	}

	void pushToClient(int code) throws IOException {
		if (null == headInfo) {
			code = 500;
		}
		createHeadInfo(code);
		
		bw.append(headInfo.toString());
		bw.append(content.toString());
	//	System.out.println(content.toString());
		bw.flush();
	}

	public void close() {
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// CloseUtil.closeIO(bw);
	}

}
