package com.bingoogol.mobilesafe;

public interface IService {

	/**
	 * 调用服务里面的方法
	 * 临时停止对某个包名的保护
	 */
	
	public void callTempStopProtect(String packname);
}
