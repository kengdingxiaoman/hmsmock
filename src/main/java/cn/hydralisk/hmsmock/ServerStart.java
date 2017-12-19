package cn.hydralisk.hmsmock;

import cn.hydralisk.hmsmock.server.Server;

import java.io.IOException;

/**
 * 启动入口
 */
public class ServerStart{

    public static void main(String args[]) throws IOException {
        Server server = new Server();
        server.service();
    }
}
