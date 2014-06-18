package com.bingoogol.socketserver;

import com.bingoogol.socketserver.util.ByteUtil;
import com.bingoogol.socketserver.util.Constants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author bingoogol@sina.com 14-3-5.
 */
public class UdpServer {
    private DatagramSocket ds;

    public UdpServer() {
        try {
            ds = new DatagramSocket(Constants.net.SERVER_UDP_PORT);
            start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        println("UdpServer 等待接收");
                        byte[] receiveBuf = new byte[1024];
                        DatagramPacket receivedDP = new DatagramPacket(receiveBuf, receiveBuf.length);
                        ds.receive(receivedDP);
                        println("UdpServer 接收完毕");
                        send(receivedDP.getAddress(), receivedDP.getPort());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void println(String msg) {
        System.out.println(msg);
    }

    public void send(InetAddress remoteInetAddress, int port) throws Exception {
        println("UdpServer 开始发送");
        // 机器名字节数组
        byte[] msgBuf = InetAddress.getLocalHost().getHostName().getBytes("UTF-8");

        int len = msgBuf.length;

        byte[] lenBuf = ByteUtil.shortToBytes((short) len);

        byte[] sendBuf = new byte[lenBuf.length + msgBuf.length];

        System.arraycopy(lenBuf, 0, sendBuf, 0, lenBuf.length);
        System.arraycopy(msgBuf, 0, sendBuf, lenBuf.length, msgBuf.length);

        DatagramPacket sendDP = new DatagramPacket(sendBuf, sendBuf.length, remoteInetAddress, port);
        ds.send(sendDP);
        System.out.println("UdpServer 发送完毕");
    }

}
