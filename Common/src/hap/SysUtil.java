package hap;

import java.io.File;


public class SysUtil
{
public static String getDirectoryOfJar( Class clazz )
{
	return new File( clazz.getProtectionDomain().getCodeSource().getLocation().getPath() ).getParent();
}
}
