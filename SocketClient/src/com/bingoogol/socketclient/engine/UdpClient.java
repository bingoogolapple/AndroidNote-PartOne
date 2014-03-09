package com.bingoogol.socketclient.engine;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import com.bingoogol.socketclient.model.Endpoint;
import com.bingoogol.socketclient.util.ByteUtil;
import com.bingoogol.socketclient.util.Constants;
import com.bingoogol.socketclient.util.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class UdpClient {
    private static final String TAG = "UdpClient";
    private static UdpClient mInstance = new UdpClient();
    private static Context mContext;
    private static String mIpAddressPrefix;
    private static boolean mIsScan = false;

    private static HashSet<Endpoint> mEndpoints;
    private static DatagramSocket mDatagramSocket;

    private UdpClient() {
    }

    public static UdpClient getInstance() {
        return mInstance;
    }

    public static void init(Context context) {
        mContext = context;
        mEndpoints = new HashSet<Endpoint>();
    }

    /**
     * 扫描局域网内ip，找到对应服务器
     */
    public static void startScan() {
        try {
            mIsScan = true;
            mIpAddressPrefix = getLocalIpAddressPrefix();
            mEndpoints.clear();
            mDatagramSocket = new DatagramSocket(Constants.net.CLIENT_UDP_PORT);
            startReceive();
            // 创建254个线程分别去ping
            for (int i = 2; i < 256; i++) {
                new Thread(new SendUDPTask(mIpAddressPrefix + i)).start();
            }
        } catch (Exception e) {
            Log.e(TAG, "打开datagramSocket出错" + e.getLocalizedMessage());
        }
    }

    private static void startReceive() {
        new Thread(new Runnable() {
            public void run() {
                while (mIsScan) {
                    try {
                        byte[] receiveBuf = new byte[1024];
                        DatagramPacket receivedDP = new DatagramPacket(receiveBuf, receiveBuf.length);
                        mDatagramSocket.receive(receivedDP);
                        ByteArrayInputStream bais = new ByteArrayInputStream(receiveBuf);
                        // 获取参数的byte长度
                        byte[] lens = new byte[2];
                        bais.read(lens);
                        short len = ByteUtil.bytesToShort(lens);
                        // 获取参数
                        if (len > 0) {
                            byte[] msgs = new byte[len];
                            bais.read(msgs, 0, len);
                            String serverName = new String(msgs, "UTF-8");
                            Log.i(TAG, "receive " + receivedDP.getAddress().getHostAddress());
                            mEndpoints.add(new Endpoint(receivedDP.getAddress().getHostAddress(), serverName));
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "接收失败  " + e.getLocalizedMessage());
                    }
                }
            }
        }).start();
    }

    public static ArrayList<Endpoint> getEndpoints() {
        ArrayList<Endpoint> result = new ArrayList<Endpoint>();
        Iterator<Endpoint> iterators = mEndpoints.iterator();
        while (iterators.hasNext()) {
            result.add(iterators.next());
        }
        return result;
    }

    public static void stopScan() {
        mIsScan = false;
        if (mDatagramSocket != null) {
            mDatagramSocket.close();
        }
    }


    /**
     * 获取ip前缀
     *
     * @return
     */
    private static String getLocalIpAddressPrefix() {
        String ipAddress = getLocalIpAddress();
        return ipAddress.substring(0, ipAddress.lastIndexOf(".") + 1);
    }

    /**
     * 获取本机IP
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    private static String getLocalIpAddress() {
        WifiInfo wifiInfo = ((WifiManager) mContext.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        return Formatter.formatIpAddress(wifiInfo.getIpAddress());
    }

    private static class SendUDPTask implements Runnable {
        private byte[] sendBuf = new byte[0];
        private String currentIP;

        public SendUDPTask(String currentIP) {
            this.currentIP = currentIP;
        }

        @Override
        public void run() {
            try {
                InetAddress remoteInetAddress = InetAddress.getByName(currentIP);
                if (remoteInetAddress.isReachable(Constants.net.SCAN_TIME_OUT)) {
                    Logger.i(TAG, "send:" + currentIP);
                    mDatagramSocket.send(new DatagramPacket(sendBuf, sendBuf.length, remoteInetAddress, Constants.net.SERVER_UDP_PORT));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}