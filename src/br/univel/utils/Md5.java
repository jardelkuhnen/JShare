package br.univel.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Md5 {

	public static String getMD5(String name) {
		InputStream fis;
		try {
			fis = new FileInputStream(name);

			byte[] buffer = new byte[1024];
			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;

			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);

			fis.close();
			return complete.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
