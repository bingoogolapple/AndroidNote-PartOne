package com.bingoogol.socketclient.util;

/**
 * Created by bingoogol@sina.com on 14-3-9.
 */
public final class Constants {
    public static final class net {
        public static final int CLIENT_UDP_PORT = 5081;
        public static final int SERVER_UDP_PORT = 5082;
        public static final int SERVER_TCP_PORT = 5083;

        public static final int TCP_TIME_OUT = 3000;
        public static final int SCAN_TIME_OUT = 1000;
        public static final int SCAN_TOTAL_TIME = 5000;
    }

    public static final class what {
        public static final int SUCCESS = 1;
        public static final int FAILURE = 2;
        public static final int PROGRESS = 3;

    }
}
