package com.bingoogol.socketserver;

/**
 * Created by bingoogol@sina.com on 14-3-9.
 */
public class Start {
    public static void main(String[] args) throws Exception {
        new UdpServer();
        new TcpServer();
    }
}
