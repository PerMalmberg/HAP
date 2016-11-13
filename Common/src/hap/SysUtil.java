package hap;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;


public class SysUtil {
	public static String getDirectoryOfJar(Class clazz) {
		return new File( clazz.getProtectionDomain().getCodeSource().getLocation().getPath() ).getParent();
	}
}
