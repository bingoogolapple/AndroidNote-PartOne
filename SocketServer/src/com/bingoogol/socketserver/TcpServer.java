package com.bingoogol.socketserver;

import com.bingoogol.socketserver.util.Constants;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author bingoogol@sina.com 14-3-5.
 */
public class TcpServer {
    private ServerSocket mServerSocket;

    public TcpServer() {
        try {
            mServerSocket = new ServerSocket(Constants.net.SERVER_TCP_PORT);
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("TcpServer 等待链接");
                while (true) {
                    try {
                        new ServerSocketTask(mServerSocket.accept());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
