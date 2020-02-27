package mavenUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandCreator {

	private static String REPOSITORY_PATH = "C:\\\\Users\\\\xxxx\\\\.m2\\\\repository";
	private static String CENTRAL_URL=" http://xxx.xxx.xxx.xxx/maven-central";
	private static String COMMAND="curl -v -u admin:xxxx --upload-file ";
	
	public static void main(String... args) {

		// key:filename, value:url
		Map<String, String> resultMap = new LinkedHashMap<String, String>();

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File file, String str) {

				if (str.endsWith("sha1")) {
					return false;
				}
				if (str.endsWith("repositories")) {
					return false;
				} else {
					return true;
				}
			}
		};

		readFileList(filter, REPOSITORY_PATH, resultMap);

		ArrayList<String> commands = new ArrayList<String>();
		for(String key:resultMap.keySet()) {
			resultMap.put(key,createURL(key));
			commands.add(COMMAND+key.replaceAll("\\\\", "\\\\\\\\")+CENTRAL_URL+createURL(key));
		}
		
		for(String command : commands) {
			System.out.println(command);
		}
		
	}

	public static void readFileList(FilenameFilter filter, String repositoryPath, Map<String, String> resultMap) {

		File[] list = new File(repositoryPath).listFiles(filter);

		for (int i = 0; i < list.length; i++) {

			if (list[i].isFile()) {
				resultMap.put(list[i].toString(), "");

			} else if (list[i].isDirectory()) {
				readFileList(filter, list[i].getAbsolutePath(), resultMap);
			}
		}
	}
	
	public static String createURL(String path) {
		String URL=path.replaceFirst(REPOSITORY_PATH, "");
		URL=URL.replaceAll("\\\\", "/");
		URL=URL.replaceAll("/[^/]*$", "/");
		return URL;
	}
}
