package hap;

import java.io.File;
import java.net.URL;


public class SysUtil {
	public static String getLocationOfJar(Class clazz) {
		String path;

		ClassLoader loader = ClassLoader.getSystemClassLoader();
		URL url = loader.getResource(".");
		try {
			path = url.getPath();
		} catch (NullPointerException ex) {
			path = ".";
		}

		return path;
	}

	public static String getDirectoryOfJar(Class clazz) {
		String fullPath = getLocationOfJar(clazz);
		File f = new File(fullPath);
		return f.getParent();
	}
}
