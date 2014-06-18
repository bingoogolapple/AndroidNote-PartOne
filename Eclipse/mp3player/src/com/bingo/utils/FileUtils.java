package com.bingo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.bingo.bean.Mp3Info;

public class FileUtils {
	private String SDCardRoot;
	public FileUtils() {
		//得到当前外部存储设备的目录
		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	/**
	 * 在SD卡上创建文件
	 * @param fileName
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public File createFileInSDCard(String fileName, String dir) throws IOException {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}
	/**
	 * 在SD卡上创建目录
	 * @param dir
	 * @return
	 */
	public File createSDDir(String dir) {
		File dirFile = new File(SDCardRoot + dir);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile;
	}
	/**
	 * 判断SD卡上的文件是否存在
	 * @param fileName
	 * @param path
	 * @return
	 */
	public boolean isFileExit(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		return file.exists();
	}
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * @param path
	 * @param fileName
	 * @param is
	 * @return
	 */
	public File write2SDFromInput(String path, String fileName, InputStream is) {
		File file = null;
		OutputStream os = null;
		try {
			file = createFileInSDCard(fileName, path);
			os = new FileOutputStream(file);
			byte[] buffer = new byte[4*1024];
			int len = 0;
			while((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	public List<Mp3Info> getMp3Files(String path) {
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		File fileDir = new File(SDCardRoot + path);
		File[] files = fileDir.listFiles();
		for(File file : files) {
			if(file.getName().endsWith("mp3")) {
				Mp3Info mp3Info = new Mp3Info();
				mp3Info.setMp3Name(file.getName());
				mp3Info.setMp3Size(file.length() + "");
				mp3Infos.add(mp3Info);
			}
		}
		return mp3Infos;
	}
}
