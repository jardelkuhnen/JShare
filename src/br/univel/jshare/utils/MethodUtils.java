package br.univel.jshare.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MethodUtils {

	public String getExtension(String name) {

		String extension = "";

		int i = name.lastIndexOf('.');
		if (i > 0) {
			extension = name.substring(i+1);
		}

		return extension;
	}

	public String getMD5(String path) {

		InputStream fis;
		try {
			fis = new FileInputStream(path);

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


			return getString(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getString(byte[] buffer) {
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length; i++) {
			if ((0xff & buffer[i]) < 0x10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(0xff & buffer[i]));
		}
		String md5 = sb.toString();
		
		return md5;
	}

	public String getNome(String nome) {

		int pos = nome.lastIndexOf(".");
		if (pos > 0) {
			nome = nome.substring(0, pos);
		}

		return nome;
	}

}
