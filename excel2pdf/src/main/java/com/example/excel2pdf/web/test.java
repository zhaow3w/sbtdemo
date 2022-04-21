package com.example.excel2pdf.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test {
	
	//统计多少行数据并且每个文件名输出到txt
	public static void main(String[] args) {

		String upload = "E:\\exceltopdf\\upload\\";
		List<String> list = getListFiles(upload);
		try {
			FileOutputStream fileOS = new FileOutputStream(upload + "list.txt");
			for (int i = 0; i < list.size(); i++) {
				
			String a =(list.get(i)+"\r\n");
			fileOS.write(a.getBytes());
			}

			

			fileOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 遍历文件夹并获取当前文件夹下文件名
	public static List<String> getListFiles(Object obj) {
		File directory = null;
		// obj是否是file类型如果是强制转换成file文件类型
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			// 不是file类型需要obj转换成string类型放入一个file实例中
			directory = new File(obj.toString());
		}
		ArrayList<String> files = new ArrayList<String>();
		// 再创建一个files实例表示当前文件夹中有多个文件或者子文件夹
		if (directory.isFile()) {
			// 如果是文件那就把文件添加到files的实例list中
			files.add(directory.toString());
			return files;
			// 如果是文件夹，那就把文件夹遍历
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				files.addAll(getListFiles(fileOne));
			}
		}
		return files;
	}
}
