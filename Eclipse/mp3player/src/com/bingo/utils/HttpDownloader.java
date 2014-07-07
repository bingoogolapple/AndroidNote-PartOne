package com.bingo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {
	private URL url = null;
	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		BufferedReader br = null;
		try {
			//使用IO流读取数据
			br = new BufferedReader(new InputStreamReader(getInputStreamFromUrl(urlStr)));
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return sb.toString();
	}
	/**
	 * 该函数返回整形，-1代表下载文件出错，0代表下载文件成功，1代表文件已存在
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return
	 */
	public int downFile(String urlStr, String path, String fileName) {
		InputStream is = null;
		try {
			FileUtils fileUtils = new FileUtils();
			if(fileUtils.isFileExit(fileName, path)) {
				return 1;
			} else {
				is = getInputStreamFromUrl(urlStr);
				fileUtils.createSDDir(path);
				File resultFile = fileUtils.write2SDFromInput(path, fileName, is);
				if(resultFile == null) {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	/**
	 * 根据URL得到输入流
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStreamFromUrl(String urlStr) throws Exception {
		//创建一个URL对象
		url = new URL(urlStr);
		//创建一个Http连接
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream is = urlConn.getInputStream();
		return is;
	}
}
