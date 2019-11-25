package MyServer.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 
 * 
 * @author Administrator
 *
 */
public class Server {
	private ServerSocket server;
	public static final String CRLF = "\r\n";
	public static final String BLANK = " ";

	private boolean isShutDown = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server();
		System.out.println("---------start--------");//************************************
		server.start();
	}

	/**
	 * 端口设置
	 */
	public void start() {
		start(8888);
	}

	/**
	 */
	public void start(int port) {
		try {
			server = new ServerSocket(port);
			this.receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 */
	private void receive() {
		try {
			while (!isShutDown) {
				new Thread(new Dispatcher(server.accept())).start();
				//System.out.println("test get connect!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e);
		}

	}
}
