package com.huangyezhaobiao.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 服务端提供的密码加密协议
 * @author linyueyang
 *
 */
public class PasswordEncrypt {
	private static final String PUBLIC_KEY_PASSWORD = "30819f300d06092a864886f70d010101050003818d0030818902818100ad8a0b5e18d8cb90f39878e35c5e74cde91c6d0911bdbcdac1bf3e45da71f50a37839486073c9576e65bf5fb44f44b49b6a8f4781e3181fd7b77a929cb512a11afb4744461ee186ec5eead18693645dd9c383d0cac278ae75865df358f8bc71e3c13c5e3217f33bf4749116e44d9e87eba38308bd053835e2fb401d1e95ca58b0203010001";
	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * @param b
	 * @return
	 * @Description:
	 */
	private static String hexEncode(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	/**
	 * 加密密码
	 * 
	 * @param data
	 * @return
	 */
	public static String encryptPassword(String data) {
		try {
			System.out.println("encryptPassword = password" + data);
			byte[] dataBytes = data.getBytes("UTF-8");
			byte[] encryptData = encryptByPublicKey(dataBytes, PUBLIC_KEY_PASSWORD);
			return hexEncode(encryptData);
		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
			return null;
		}
	}

	/**
	 * HEX解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptHEX(String key) throws Exception {
		return hexDecode(key);
	}

	/**
	 * @param str
	 * @return
	 * @Description:
	 */
	private static byte[] hexDecode(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptHEX(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}
}
