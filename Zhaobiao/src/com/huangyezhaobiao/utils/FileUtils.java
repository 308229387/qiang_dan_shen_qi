package com.huangyezhaobiao.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.huangyezhaobiao.url.URLConstans;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileUtils {
	private static volatile File file;
	private static final String file_path = "/newfile.txt";
	private static void showAvailableBytes(InputStream in) {
		try {
			System.out.println("当前字节输入流中的字节数为:" + in.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 将埋点的数据写到文件中
	 * @param context
	 * @param json
	 */
	public static synchronized void write(Context context,String json) {
		String space = System.getProperty("line.separator");
		json = json + space;
		FileOutputStream fop = null;
		OutputStreamWriter writer = null;
		try {
			String path = context.getFilesDir().getPath();
			LogUtils.LogE("ashenhjhj", "path:"+path);
			String paths = path + file_path;
			file = new File(paths);
			fop = new FileOutputStream(file,true);
			 writer = new OutputStreamWriter(fop,"utf-8");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = json.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
			 LogUtils.LogE("ashenFile", "error write");
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
				if(writer != null){
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * 将文件的数据通过post请求上传到服务器
	 * @param context
	 */
	public static synchronized void read(Context context) {
		boolean flag = true;
		FileInputStream fin = null;
		try {
			String path = context.getFilesDir().getPath();
			LogUtils.LogE("ashenhjhj", "path:" + path);
			String paths = path + file_path;
			LogUtils.LogE("ashenhjhjededede", "path:" + paths);
			//if(flag) return;
			file = new File(paths);
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			fin = new FileInputStream(file);
			int length = fin.available();
			byte[] tempbytes = new byte[length];
			showAvailableBytes(fin);
			fin.read(tempbytes);
			String s = EncodingUtils.getString(tempbytes, "UTF-8");
			System.out.println(s.length());
			String a = GzipUtil.gzip(s);
			HttpUtils utils = new HttpUtils();
			RequestParams params = new RequestParams("UTF-8");
			params.addBodyParameter("data",a);
			LogUtils.LogE("ashenUpload", "upload content:"+a);
			utils.send(HttpMethod.POST, URLConstans.BASE_URL+"log/collection", params,new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					LogUtils.LogE("ashenUpload", "base64 decode success:"+arg0.result);
					try {
						JSONObject object = new JSONObject(arg0.result);
						String  status = object.getString("status");
						if(TextUtils.equals("0", status)){
							file.delete();
							LogUtils.LogE("ashenUpload", "delete.."+status);
						}
						LogUtils.LogE("ashenUpload", "status.."+status);
					} catch (JSONException e) {
						e.printStackTrace();
						LogUtils.LogE("ashenUpload", "wrong");
					}
				}
			});
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

	public static String BASE64Decode(String s){
		return new String(Base64.decode(s, 0));
	}
	
	/**
	 * base64编码
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String BASE64(String s) throws UnsupportedEncodingException{
		if(s==null) return null;
				return Base64.encodeToString(s.getBytes("UTF-8"), Base64.DEFAULT);
	}

	
}