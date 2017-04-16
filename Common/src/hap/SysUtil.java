package hap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class SysUtil
{
	public static String getDirectoryOfJar( Class clazz )
	{
		return new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
	}

	public static String getPathOfJar( Class clazz )
	{
		return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
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

	// Extracts files from the jar specified by the clazz into the targetPath based on decision by the provided decision maker.
	public static List<File> extractFilesFromJar( Class<?> clazz, Path targetPath, IExtractOrNot decision ) throws IOException
	{
		ArrayList<File> extractedFiles = new ArrayList<>();

		try( ZipInputStream zip = new ZipInputStream( new FileInputStream( SysUtil.getPathOfJar( clazz ) ) ) )
		{
			ZipEntry ze = zip.getNextEntry();
			while( ze != null )
			{
				if( decision.ExtractOrNot( ze ) )
				{
					Path outName = Paths.get( targetPath.toString(), ze.getName() );
					File outFile = outName.toFile();

					boolean res = true;

					// Create dir if it doesn't exist
					if( ! outFile.getParentFile().exists() )
					{
						res = outFile.getParentFile().mkdirs();
					}

					if( res && outFile.createNewFile() )
					{
						byte[] data = new byte[4096];
						try( FileOutputStream fos = new FileOutputStream( outFile ) )
						{
							int readLen = zip.read(data, 0, data.length);
							while( readLen != -1 ) {
								fos.write( data, 0, readLen );
								readLen = zip.read(data, 0, data.length);
							}
						}

						extractedFiles.add( outFile );
					}
				}
				ze = zip.getNextEntry();
			}
		}


		return extractedFiles;
	}

	public static void deleteRecursively( File path )
	{
		if( path != null )
		{
			if( path.isDirectory() )
			{
				File[] files = path.listFiles();
				if( files != null )
				{
					for( File c : files )
					{
						deleteRecursively(c);
					}
					path.delete();
				}
			}
			else
			{
				path.delete();
			}
		}
	}

	public static void deleteFolderOnExit( Path path )
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			// Delete all files in the temp folder
			SysUtil.deleteRecursively(path.toFile());
		}));
	}
}
