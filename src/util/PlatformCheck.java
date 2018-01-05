package util;

import java.util.Properties;

public class PlatformCheck {

	public static boolean isMac() {

		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if (os.startsWith("Mac") || os.startsWith("mac")) {
			return true;
		}

		return false;
	}

}
