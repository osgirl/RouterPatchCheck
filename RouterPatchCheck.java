
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.ArrayList;

public class RouterPatchCheck {

	private static final Logger logger = Logger.getLogger(RouterPatchCheck.class.getName() );
	private final static String operatingSystem = System.getProperty("os.name").toLowerCase();

	private static List<Router> noPatch = new ArrayList<>(); // List of all routers that do not require the patch.
	private static List<Router> yesPatch = new ArrayList<>(); //List of all routers that do require the patch.

	private final static String fileType = ".csv";
	private final static String fileDelimiter = ",";

	private static BufferedReader bf = null;

	private static String fileName;
	private static File file;

	public static void loggerSetup() {

		FileHandler fileHandler = null;
		logger.setUseParentHandlers(false); // Disables console output from log
		//Determine Operating System for file handling
		logger.info("Operating System: "+operatingSystem);
		
		try {

			if (isWindows()) {
				fileHandler = new FileHandler(".\\checklog.log"); // Created in current directory
			} else if (isUnix()){
				fileHandler = new FileHandler(".//checklog.log"); // Created in current directory
			} else if (isMac()) {
				fileHandler = new FileHandler(".//checklog.log"); // Created in current directory
			}

			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
		// Log create date
		DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
		Date nowDate = new Date();
		logger.info("Log Created: "+dateFormat.format(nowDate));
	}

	public static void printList(List<Router> toPatch) {
		System.out.println("------To be Patched------");
		for (Object route : toPatch) {
			System.out.println(route);
		}
	}

	public static File getFile(String name) {

		try {

			if (isWindows()) {
				file = new File(".\\"+name.concat(fileType));
			} else if (isUnix()) {
				file = new File(".//"+name.concat(fileType));
			}else if (isMac()){
				file = new File(".//"+name.concat(fileType));
			}

			logger.info("FileName: "+file.toString());

		} catch (ArrayIndexOutOfBoundsException e) {
			logger.warning("No File was Specified");
		}

		return file;
	}

	public static boolean isWindows() {
		return (operatingSystem.indexOf("win") >= 0);
	}
	
	public static boolean isUnix(){
		return (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0 || operatingSystem.indexOf("aix") > 0 );
	}
	
	public static boolean isMac() {
        return (operatingSystem.indexOf("mac") >= 0);
    }
	
	public static void parseCSV() throws IOException {

		String currentLine;

		try {
			bf = new BufferedReader(new FileReader(getFile(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while ((currentLine = bf.readLine()) != null) {
			String [] fields = currentLine.split(fileDelimiter, -1); //Ignores blank fields

			String hostname = fields[0].toLowerCase(); //There are no other routers which share the same hostname
			String ipAddress = fields[1]; //There are no other routers which share the same IP address
			String patched = fields[2].toLowerCase(); //The router has not already been patched
			String osVersion = fields[3]; //The current version of the router OS is 12 or above
			String notes = fields[4];
			
			Router router = new Router(hostname,ipAddress,patched,osVersion,notes);

			double osVersionConverted = Double.parseDouble(osVersion);
			final double currentVersion = 12;

			// Determines whether version is patch'able, or if explicitly describe as patched
			if (patched.equalsIgnoreCase("yes")) {
				noPatch.add(router);
			} else if (patched.equalsIgnoreCase("no") && osVersionConverted >= currentVersion) {
				yesPatch.add(router);
			} else if (osVersionConverted >= currentVersion) {
				yesPatch.add(router);
			} else {
				noPatch.add(router);
			}

			// comparison of objects in the list (for hostname and IP)
			for (int i = 0; i < yesPatch.size(); i++) {
				for (int j = i+1; j < yesPatch.size(); j++) {

					if (yesPatch.get(i).getHostname().equalsIgnoreCase(yesPatch.get(j).getHostname())){

						logger.log(Level.WARNING, "Matching Hostname found, please review file.");
						noPatch.add(yesPatch.get(j));
						noPatch.add(yesPatch.get(i));
						yesPatch.remove(j);
						yesPatch.remove(i);
					} else if(yesPatch.get(i).getIpAddress().equalsIgnoreCase(yesPatch.get(j).getIpAddress())) {

						logger.log(Level.WARNING, "Matching IP Address found, please review file.");
						noPatch.add(yesPatch.get(j));
						noPatch.add(yesPatch.get(i));
						yesPatch.remove(j);
						yesPatch.remove(i);
					} 
				}
			}

		}

		bf.close(); //Close BufferedReader

	}

	public static void main (String args []) throws IOException {

		fileName = args[0];

		loggerSetup();
		parseCSV();
		printList(yesPatch);

	} // Main

} // Class
