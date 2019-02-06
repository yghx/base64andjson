package com.utils.base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

public class Base64Image {
	private List<String> base64List =new ArrayList<>();
	public String image2Base64(File sourceFile) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(sourceFile);
			data = new byte[in.available()];
			in.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in == null) {
				in = null;
			} else {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String s = new String(Base64.encodeBase64(data));
		System.out.println(s);
		return s;
	}
	
	public void data2File(FileOutputStream fos,String base64) throws IOException{
		fos.write(base64.getBytes());
		fos.write("\r\n".getBytes());
	}
	
	public List<String> getBase64List(FileInputStream fis) throws IOException{
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		ArrayList<String> base64List = new ArrayList<>();
		String temp = "";
		while((temp = br.readLine() )!= null){
			base64List.add(temp);
		}
		return base64List;
	}
	
	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public void base642Image(String base64TxtFile,String destinationDirectory) throws IOException {
		List<String> base64List = getBase64List(new FileInputStream(base64TxtFile));
		for (String base64 : base64List) {
			try {
				// Base64解码
				byte[] bytes = Base64.decodeBase64(base64);
				for (int i = 0; i < bytes.length; ++i) {
					if (bytes[i] < 0) {// 调整异常数据
						bytes[i] += 256;
					}
				}
				// 生成jpeg图片
				OutputStream out = new FileOutputStream(destinationDirectory+File.separator+getUuid()+".jpg");
				// ServletOutputStream out = response.getOutputStream();
				out.write(bytes);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean image2Base64Api(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] listFiles = file.listFiles();
				for (File childrenFile : listFiles) {
					image2Base64Api(childrenFile);
				}
			} else {
				base64List.add(image2Base64(file));
			}
			return true;
		} else{
			System.out.println("file dont exist");
			return false;
		}
	}
	
	public List<String> getBase64List() {
		return base64List;
	}

	public void setBase64List(List<String> base64List) {
		this.base64List = base64List;
	}

	public void imageDirectory2TxtFile(String iamgeDirectory,String destinationTxtFile) throws IOException {
		//文件到txt
		boolean image2Base64Api = image2Base64Api(new File(iamgeDirectory));
		if(image2Base64Api){
			FileOutputStream fos = new FileOutputStream(destinationTxtFile);
			for (String base64 : getBase64List()) {
				data2File(fos, base64);
			}
			fos.close();
		}else{
			System.out.println("this opreation is fail!!");
		}
	}
	
	public static void main(String[] args) throws IOException {
		//new Base64Image().imageDirectory2TxtFile("./images", "./txts/image.txt");;
		new Base64Image().base642Image("./txts/image.txt", "./images");
	}
	
}
