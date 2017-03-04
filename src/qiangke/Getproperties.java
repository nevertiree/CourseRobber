package qiangke;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Getproperties {

	public static void main(String[] args) {
		System.out.println(getParam1());
		System.out.println(getParam2());
	}

	private static String param1;
	private static String param2;

	static {
		Properties prop = new Properties();
//		InputStream in = Object.class.getResourceAsStream("/mail.properties");
//		InputStream in = Object.class.getClassLoader().getResourceAsStream("mail.properties");
		try {
			final File input = new File("D:\\jars\\mail.properties");
			final InputStream is = new FileInputStream(input);
			prop.load(is);
			param1 = prop.getProperty("mail.smtp.auth").trim();
			param2 = prop.getProperty("mail.smtp.host").trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 私有构造方法，不需要创建对象
	 */
	private Getproperties() {
	}

	public static String getParam1() {
		return param1;
	}

	public static String getParam2() {
		return param2;
	}
}
