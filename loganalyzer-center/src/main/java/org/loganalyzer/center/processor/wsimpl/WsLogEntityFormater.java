package org.loganalyzer.center.processor.wsimpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.loganalyzer.center.model.ILogEntity;
import org.loganalyzer.center.model.wsimpl.WSLogEntity;
import org.loganalyzer.center.processor.IFormater;
import org.loganalyzer.center.processor.wsimpl.model.WSBasicFormatLogEntity;

public class WsLogEntityFormater implements IFormater {

	private static Pattern entityPattern = Pattern
			.compile("\\[([^]]+)\\]\\s([0-9a-z]{8})\\s([a-zA-z]+)\\s+([FEWAICDORZ])\\s([\\s\\S]*)");

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yy HH:mm:ss:SSS");

	public WsLogEntityFormater() {
		dateFormat.setTimeZone(TimeZone.getTimeZone("EDT"));
	}

	@Override
	public ILogEntity format(ILogEntity oldEntity) {
		String value = oldEntity.getValue().toString();
		Matcher m = entityPattern.matcher(value);
		if (m.matches()) {
			try {
				String timestamp = m.group(1);
				String threadId = m.group(2);
				String shortName = m.group(3);
				String eventType = m.group(4);
				String message = m.group(5);
				long tt = parseTime(timestamp);
				Set<String> exceptions = findException(message); // may null
				String exceptionClass = findExceptionClass(message); // may null

				WSBasicFormatLogEntity wbfle = new WSBasicFormatLogEntity();
				wbfle.setTimestamp(tt);
				wbfle.setThreadId(threadId);
				wbfle.setShortName(shortName);
				wbfle.setEventType(eventType);
				wbfle.setMessage(message);
				wbfle.setExceptions(exceptions);
				wbfle.setExceptionClass(exceptionClass);

				WSLogEntity wle = new WSLogEntity();
				wle.setMeta(oldEntity.getMeta());
				wle.setValue(wbfle);
				return wle;
			} catch (Exception e) {
				System.err.println(value);
				return null;
			}
		} else {
			System.err.println(value);
			return null;
		}
	}

	private long parseTime(String source) throws ParseException {
		return dateFormat.parse(source).getTime();
	}

	private Set<String> findException(String s) {
		Set<String> eL = new HashSet<String>();
		StringBuffer sb = new StringBuffer();
		char c;
		int dotcount = 0;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (c == ':' && dotcount > 2 && sb.length() > 0) {
				eL.add(sb.toString());
				dotcount = 0;
				continue;
			}
			if ((c > 'a' && c < 'z') || (c > 'A' && c < 'Z')
					|| (c > '0' && c < '9')) {
				sb.append(c);
			} else if (c == '.') {
				sb.append(c);
				dotcount++;
			} else {
				if (sb.length() > 0) {
					sb = null;
					sb = new StringBuffer();
					dotcount = 0;
				}
			}
		}
		return eL.size() > 0 ? eL : null;
	}

	private String findExceptionClass(String s) {
		String exceptionClass = null;
		Scanner scanner = new Scanner(s);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if (line.startsWith("at ")){
				int left = line.indexOf("(");
				if (left > 0) {
					String c_m = line.substring(3, left);
					int m = c_m.lastIndexOf('.');
					if (m > 0) {
						exceptionClass = c_m.substring(0, m);
					}
				}
			}
		}
		scanner.close();
		return exceptionClass;
	}

}
