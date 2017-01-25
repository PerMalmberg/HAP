package hap;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SysUtil
{
	public static String getDirectoryOfJar( Class clazz )
	{
		return new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
	}

	public static String getFullOrRelativePath( Class relativeTo, String dir )
	{
		Path p = Paths.get(dir);
		if( p.getRoot() == null )
		{
			p = Paths.get(getDirectoryOfJar(relativeTo), dir);
		}

		return p.toString();
	}

	public static String getNameOfJarForClass(Class clazz)
	{
		return new File(clazz.getProtectionDomain()
				.getCodeSource()
				.getLocation()
				.getPath())
				.getName();
	}

	public static boolean isRunningFromJAR(Class clazz)
	{
		File f = new File( clazz.getProtectionDomain()
				.getCodeSource()
				.getLocation().getPath() );

		return f.isFile() && getNameOfJarForClass(clazz).toLowerCase().endsWith(".jar");
	}
}
