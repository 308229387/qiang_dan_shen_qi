package com.huangye.commonlib.file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class FileUtils {

	private static void showAvailableBytes(InputStream in) {
		try {
			System.out.println("当前字节输入流中的字节数为:" + in.available());
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//写数据
	public static void write() {
		FileOutputStream fop = null;
		File file;
		String content = "This is the text content";
		try {
			file = new File("/data/data/com.huangyezhaobiao/files/newfile.txt");
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//读取数据
	public static void read(String path,String name) {
		//File file = new File("/data/data/com.huangyezhaobiao/files/newfile.txt");
		
		File file = new File(path+name);
		FileInputStream fin = null;
		try {

			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			fin = new FileInputStream(file);
			int length = fin.available();
			byte[] tempbytes = new byte[length];
			showAvailableBytes(fin);
			fin.read(tempbytes);
			String s = EncodingUtils.getString(tempbytes, "UTF-8");
			System.out.println(s);

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e1) {
				}
			}
		}

	}

	private void uploadFile() {
		String end = "/r/n";
		String Hyphens = "--";
		String boundary = "*****";
		// 要上传的本地文件路径
		String uploadFile = "/data/data/com.example.l1/haha.txt";
		// 上传到服务器的指定位置
		String actionUrl = "http://192.168.100.100:8080/upload/upload.jsp";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设定传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设定DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设定每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据到缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将数据写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(Hyphens + boundary + Hyphens + end);
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			System.out.println("上传成功");
			ds.close();
		} catch (Exception e) {
			System.out.println("上传失败" + e.getMessage());
		}

	}
	
	
	/**
	* 判断手机是否有SD卡。
	* 
	* @return 有SD卡返回true，没有返回false。
	*/
	public static boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
	
	
	
	// 写数据到SD中的文件
	public void writeFileSdcardFile(String fileName, String write_str) throws IOException {
		try {

			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	  
	// 读SD中的文件
	public String readFileSdcardFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];
			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	

}