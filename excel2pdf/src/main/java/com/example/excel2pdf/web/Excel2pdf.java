package com.example.excel2pdf.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.cells.License;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
@EnableScheduling
@RestController
public class Excel2pdf implements ApplicationRunner {
	/**
	 * excel文件导出为PDF文件
	 * 
	 * @param Address 需要转化的Excel文件地址，
	 *
	 */
	@RequestMapping("/exc2pdf")
	public static void excel2pdf(String Address) {
		if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
			return;
		}
		try {
			// String upload ="/pyfile/upfile/upload/";
			// String zipfile ="/pyfile/upfile/zipfile/";
			String upload = "E:\\exceltopdf\\upload\\";
			String zipfile = "E:\\exceltopdf\\zipfile\\";
			// String uppath =System.getProperty("user.dir");
			// uppath =uppath+"\\";
			// Address =upload+Address+".xls";
			// SimpleDateFormat date0 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String date1 = date0.format(new Date()).toString();
			// File pdfFile = new File(zipfile+"456"+date1+".pdf"); // 输出路径
			File pdfFile = new File(zipfile + Address + ".pdf"); // 输出路径
			Address = upload + Address + ".xlsx";
			System.out.println(Address);
			FileInputStream excelstream = new FileInputStream(Address);
			Workbook wb = new Workbook(excelstream);// excel路径，这里是先把数据放进缓存表里，然后把缓存表转化成PDF
			FileOutputStream fileOS = new FileOutputStream(pdfFile);
			PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
			pdfSaveOptions.setOnePagePerSheet(true);// 参数true把内容放在一张PDF页面上；
			wb.save(fileOS, pdfSaveOptions);
			fileOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取认证，去除水印
	public static boolean getLicense() {
		boolean result = false;
		try {
			// 这个文件应该是类似于密码验证(证书？)，用于获得去除水印的权限
			InputStream is = Excel2pdf.class.getClassLoader().getResourceAsStream("license.xml");
			License aposeLic = new License();
			aposeLic.setLicense(is);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 请求路径
	@RequestMapping("/hello")
	public String hello() {
		return "hello spring boot";
	}

	// 遍历文件夹并获取当前文件夹下文件名
	
	public static List<String> getListFiles(Object obj) {
		File directory = null;
		//obj是否是file类型如果是强制转换成file文件类型
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
		//不是file类型需要obj转换成string类型放入一个file实例中 
			directory = new File(obj.toString());
		}
		ArrayList<String> files = new ArrayList<String>();
		//再创建一个files实例表示当前文件夹中有多个文件或者子文件夹
		if (directory.isFile()) {
			//如果是文件那就把文件添加到files的实例list中
			files.add(directory.toString());
			return files;
			//如果是文件夹，那就把文件夹遍历
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				files.addAll(getListFiles(fileOne));
			}
		}
		return files;
	}

	// 实现了ApplicationRunner会自动启动，一般用于加载日志和配置文件
	//@Scheduled(cron = "0/50 * * * * ? ")
	public void timejob() throws Exception {
		// 验证License 若不验证则转化出的pdf文档会有水印产生
		if (!getLicense()) { 
			return;
		}
		String upload = "E:\\exceltopdf\\upload\\";
		// String zipfile ="E:\\exceltopdf\\zipfile\\";
		// 是否需要加日期时间戳
		// SimpleDateFormat date0 = new SimpleDateFormat("yyyyMMddHHmmss");
		// String date1 = date0.format(new Date()).toString();

		List<String> list = getListFiles(upload);
		//System.out.println(list);

		for (int i = 0; i < list.size(); i++) {
			// 遍历文件名并替换excel转换成pdf的文件名
			File pdfFile = new File(list.get(i).toString().replace("upload", "zipfile").replace("xlsx", "pdf")); // 输出路径
			// 把当前要转换的excel放入输入流中内存当中
			FileInputStream excelstream = new FileInputStream(list.get(i));
			// excel路径，这里是先把数据放进缓存表里，然后把缓存表转化成PDF
			Workbook wb = new Workbook(excelstream);
			// 打开输出流并把转化的pdf数据输出到文件路径中File pdfFile
			FileOutputStream fileOS = new FileOutputStream(pdfFile);
			// 调用pdf相关jar包的实例
			PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
			// 参数true把内容放在一张PDF页面上；
			pdfSaveOptions.setOnePagePerSheet(true);
			// 写入文件中
			wb.save(fileOS, pdfSaveOptions);
			// 关闭输出流，释放内存，已经抛异常了不用trycatch指定异常
			fileOS.close();

		}
		//生成后可以加一个去判断的方法,如果存在就不执行了
		//如果对应日期有就不生成了
		 
	}
	
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		timejob();
	}

	
	
}
