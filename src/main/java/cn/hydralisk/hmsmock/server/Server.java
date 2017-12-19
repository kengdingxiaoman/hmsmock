package cn.hydralisk.hmsmock.server;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.util.CommonUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server端
 * 
 * @author master.yang
 * 
 */
public class Server {

	private ServerSocket serverSocket;
	private ExecutorService executorService;

	public Server() throws IOException {
		serverSocket = new ServerSocket(HmsConstants.SERVER_PORT);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * HmsConstants.THREAD_POOL_SIZE);

		CommonUtils.println("HmsMock启动!");
	}

	public void service() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				executorService.execute(new CommandHandler(socket));
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
