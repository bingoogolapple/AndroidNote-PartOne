package com.bingoogol.socketserver;

import com.bingoogol.socketserver.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by bingoogol@sina.com on 14-3-9.
 */
public class ServerSocketTask {
    private Socket mScoSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Scanner scanner;

    public ServerSocketTask(Socket socket) {
        mScoSocket = socket;
        try {
            scanner = new Scanner(System.in);
            mInputStream = mScoSocket.getInputStream();
            mOutputStream = mScoSocket.getOutputStream();
            println("新用户：" + StreamUtil.receiveString(mInputStream));
            println("品牌：" + StreamUtil.receiveString(mInputStream));
            StreamUtil.sendString(mOutputStream, UUID.randomUUID().toString());
            new Thread(new SendTask()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendTask implements Runnable {
        private int commond = 0;

        @Override
        public void run() {
            try {
                while (true) {
                    printInfo();
//                    commond = scanner.nextInt();
                    commond = 3;
                    switch (commond) {
                        case 1:
                            // 发送数字
                            StreamUtil.sendInt(mOutputStream, 1);
                            println("请输入要发送的数字");
                            StreamUtil.sendInt(mOutputStream, scanner.nextInt());
                            break;
                        case 2:
                            // 发送字符
                            StreamUtil.sendInt(mOutputStream, 2);
                            println("请输入要发送的字符串");
                            StreamUtil.sendString(mOutputStream, scanner.nextLine());
                            break;
                        case 3:
                            // 发送文件
                            StreamUtil.sendInt(mOutputStream, 3);
                            int isReceive = StreamUtil.receiveInt(mInputStream);
                            if (isReceive == 10086) {
                                println("客户端同意下载");
                            } else if (isReceive == 1000) {
                                println("客户端拒绝下载");
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getClass());
            }
        }
    }

    private void printInfo() {
        println("请选择：");
        println("1.发送数字");
        println("2.发送字符串");
        println("3.发送文件");
    }

    private void println(String msg) {
        System.out.println(msg);
    }
}
