package com.huangyezhaobiao.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Base64;

public class GzipUtil {

	/*public static void main(String[] args) throws IOException {
		String s = "The client and server have to agree on how to communicate; part of this is whether the communication can be compressed. HTTP was designed as a request/response model, and the original creation was almost certainly envisioned to always have small requests and potentially large responses. Compression is not required to implement HTTP, there are both servers and clients that don't support it."
				+ "HTTP compression is implemented by the client saying it can support compression, and if the server sees this in the request and it supports compression it can compress the response. To compress the request the client would have to have a pre-request that actually negotiated that the request would be made compressed OR it would have to require compression as a supported encoding for ALL requests."
				+ "The major error is converting the compressed bytes to a String. Java separates binary data (byte[], InputStream, OutputStream) from text (String, char, Reader, Writer) which internally is always kept in Unicode. A byte sequence does not need to be valid UTF-8. You might get away by converting the bytes as a single byte encoding";
		System.out.println(s.length());
		System.out.println(gzip(s).length());
		System.out.println(gzip(s));
		System.out.println(gunzip(gzip(s)));
		System.out.println(s.getBytes().length);
		System.out.println(gzip(s.getBytes()).length);
		System.out.println(gunzip(gzip(s.getBytes())).length);
	}*/

	public static byte[] gzip(byte[] data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		try {
			gzip.write(data);
		} finally {
			gzip.close();
		}
		return out.toByteArray();
	}

	public static byte[] gunzip(byte[] compressedData) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(
				compressedData));
		try {
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = gis.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			return out.toByteArray();
		} finally {
			gis.close();
		}
	}

	public static String gzip(String str) throws IOException {
		byte[] zipBytes = gzip(str.getBytes("UTF-8"));
		byte[] base64Byte = Base64.encode(zipBytes, 0);
		return new String(base64Byte, "UTF-8");
	}

	public static String gunzip(String compressedStr) throws IOException {
		byte[] srcZipBytes = Base64.decode(compressedStr.getBytes("UTF-8"), 0);
		byte[] srcBytes = gunzip(srcZipBytes);
		return new String(srcBytes, "UTF-8");
	}
}
