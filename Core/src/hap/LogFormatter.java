package hap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	@Override
	public String format(LogRecord record) {
		String buff = calcDate(record.getMillis());
		buff += " [";
		buff += record.getLevel();
		buff += "] ";
//		buff+= record.getSourceClassName();
//		buff+= ".";
//		buff+= record.getSourceMethodName();
//		buff+= " ";
		buff += record.getMessage();
		buff += String.format("%n");
		return buff;
	}

	private String calcDate(long millisecs) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(millisecs);
		return dateFormat.format(date);
	}


}
