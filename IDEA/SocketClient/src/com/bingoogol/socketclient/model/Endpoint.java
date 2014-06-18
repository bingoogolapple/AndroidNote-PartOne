package com.bingoogol.socketclient.model;

/**
 * @author bingoogol@sina.com 14-3-5.
 */
public class Endpoint {
    public String name;
    public String ip;

    public Endpoint(String ip, String name) {
        this.name = name;
        this.ip = ip;
    }
}
