package hap;

import java.util.zip.ZipEntry;

/**
 * Created by Per Malmberg on 2017-02-07.
 */
public interface IExtractOrNot
{
	// Returns true if the file should be extracted
	boolean ExtractOrNot( ZipEntry ze);
}
