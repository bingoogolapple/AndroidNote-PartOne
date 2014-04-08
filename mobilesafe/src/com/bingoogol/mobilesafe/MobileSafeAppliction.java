package com.bingoogol.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.app.Application;
import android.os.Build;
import android.os.Environment;

/**
 * 基类 记录维护应用程序全局的状态
 * @author Administrator
 *
 */
public class MobileSafeAppliction extends Application {

	/**
	 * 在应用程序创建的时候 被调用.
	 * 当应用程序的进程创建的时候 
	 * 在被的对象创建之前调用的方法. 
	 * 相当于整个应用初始化的操作
	 */
	@Override
	public void onCreate() {
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
		super.onCreate();
	}

	private class MyExceptionHandler implements UncaughtExceptionHandler{
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			
			StringWriter sw =  new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			
			ex.printStackTrace(pw);
			
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("错误时间:"+System.currentTimeMillis()+"\n");
				//收集设备的信息 时间
				Field[]  fields = Build.class.getFields();
				for(Field field: fields){
					
					String value = field.get(null).toString();
					String name = field.getName();
					sb.append(name+":"+value+"\n");
				}
				String errormsg = sw.toString();
				sb.append(errormsg);
				
				File file = new File(Environment.getExternalStorageDirectory(),"mobilesafeerror.log");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("产生了异常,但是被哥给捕获了.");
			//原地复活不可能.
			//自杀
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

}
