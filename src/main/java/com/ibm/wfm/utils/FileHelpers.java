package com.ibm.wfm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileHelpers {
	/**
	 * The file separator string, "/", used in the formation of the URL path.
	 */
	public static final String urlfileseparator = "/";

	public static final int TYPE_DIRECTORY = 1;
	public static final int TYPE_FILE = 2;
	public static final int TYPE_MASK = 3;
	public static final int TYPE_INVALID = 0;

	public static void main(String args[]) {
		String filePattern = "";
		filePattern = "*.ddl";
		String[] filePatterns = filePattern.split(",");
		try {
			for (String fp: filePatterns) {
				List<String> files = FileHelpers.getFileList("/Users/steve/Downloads/allArtifacts/"+fp);
				for (String file: files) {
					System.out.println("");
					System.out.println(file);
					List<String> sqlStatements = FileHelpers.fileToSqlList(file);
					for (String sqlStatment: sqlStatements) {
						System.out.println(sqlStatment);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	FileHelpers() {
	}

	public static boolean stringToFile(String fileNm, String s) {
		try {
			FileWriter fw = new FileWriter(fileNm);
			fw.write(s);
			fw.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String fileToString(String fileNm) throws IOException {
		return fileToString(fileNm, null, null);
	}

	public static String fileToString(String fileNm, String lineEndString) throws IOException {
		return fileToString(fileNm, null, null);
	}
	/*
	 * public static String fileToString(String fileNm, String lineEndString, String
	 * ignoreAfterStr) throws IOException { StringBuffer sb = new StringBuffer();
	 * FileReader frdr = new FileReader(fileNm); LineNumberReader lnrdr = new
	 * LineNumberReader(frdr); String lineBuffer = null; while ((lineBuffer =
	 * lnrdr.readLine()) != null) { int commentOffset = -1; if
	 * (ignoreAfterStr!=null) commentOffset = lineBuffer.indexOf(ignoreAfterStr); if
	 * (commentOffset!=0) { if (commentOffset>0) lineBuffer =
	 * lineBuffer.substring(0, commentOffset);
	 * sb.append(lineBuffer).append(lineEndString == null ? "" : lineEndString); } }
	 * return sb.toString(); }
	 */

	public static boolean echoCsv(String sourceFileName, String targetFileName) {
		return echoCsv(sourceFileName, ",", targetFileName);
	}
	
	public static void compare2Files(String csv1, String csv2) throws IOException, FileNotFoundException {
		BufferedReader csvFile1 = new BufferedReader(new FileReader(csv1));
		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		int cnt=0;
		

		System.out.println("Starting compare2Csv "+ new java.util.Date());
		System.out.println("  reading 1st csv "+ new java.util.Date());
		String dataRow1 = csvFile1.readLine();
		while (dataRow1 != null) {
			al1.add(dataRow1);

			dataRow1 = csvFile1.readLine(); // Read next line of data.
			if (++cnt%10000 == 0) System.out.println(cnt);
			else if(cnt%1000 == 0) System.out.print(".");
		}
		csvFile1.close();
		
		BufferedReader csvFile2 = new BufferedReader(new FileReader(csv2));
		String dataRow2 = csvFile2.readLine();
		System.out.println("  reading 2nd csv "+ new java.util.Date());
		cnt=0;
		while (dataRow2 != null) {
			al2.add(dataRow2);
			dataRow2 = csvFile2.readLine(); // Read next line of data.
			if (++cnt%10000 == 0) System.out.println(cnt);
			else if(cnt%1000 == 0) System.out.print(".");
		}
		csvFile2.close();
		
		System.out.println("  Removing matching rows from 2nd file to first "+ new java.util.Date());
		cnt=0;
		for (String bs : al2) {
			al1.remove(bs);
			if (++cnt%10000 == 0) System.out.println(cnt);
			else if(cnt%1000 == 0) System.out.print(".");
		}
		
		int size = al1.size();
		System.out.println(size);
		while (size != 0) {
			size--;
			System.out.println("" + al1.get(size));
		}

		
		
	}

	public static void compare2Csv(String csv1, String csv2) throws IOException, FileNotFoundException {
		BufferedReader csvFile1 = new BufferedReader(new FileReader(csv1));
		String dataRow1 = csvFile1.readLine();
		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		int cnt=0;

		System.out.println("Starting compare2Csv "+ new java.util.Date());
		System.out.println("  reading 1st csv "+ new java.util.Date());
		while (dataRow1 != null) {
			String[] dataArray1 = dataRow1.split(",");
			for (String item1 : dataArray1) {
				al1.add(item1);
			}

			dataRow1 = csvFile1.readLine(); // Read next line of data.
			if (++cnt%10000 == 0) System.out.println(cnt);
			else if(cnt%1000 == 0) System.out.print(".");
		}
		
		csvFile1.close();

		BufferedReader csvFile2 = new BufferedReader(new FileReader(csv2));
		String dataRow2 = csvFile2.readLine();
		System.out.println("  reading 2nd csv "+ new java.util.Date());
		cnt=0;
		while (dataRow2 != null) {
			String[] dataArray2 = dataRow2.split(",");
			for (String item2 : dataArray2) {
				al2.add(item2);

			}
			dataRow2 = csvFile2.readLine(); // Read next line of data.
			if (++cnt%10000 == 0) System.out.println(cnt);
			else if(cnt%1000 == 0) System.out.print(".");
		}
		csvFile2.close();
		
		System.out.println("  Removing matching rows from 2nd file to first "+ new java.util.Date());
		cnt=0;
		for (String bs : al2) {
			al1.remove(bs);
			if (++cnt%10000 == 0) System.out.println(cnt);
			else if(cnt%1000 == 0) System.out.print(".");
		}

		int size = al1.size();
		System.out.println(size);
		while (size != 0) {
			size--;
			System.out.println("" + al1.get(size));
		}
		
		/*

		try {
			FileWriter writer = new FileWriter("");
			while (size != 0) {
				size--;
				writer.append("" + al1.get(size));
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

	}

	public static boolean echoCsv(String sourceFileName, String delimiter, String tagetFileName) {

		FileReader frdr;
		try {
			frdr = new FileReader(sourceFileName);

			FileWriter fw = new FileWriter(tagetFileName);

			LineNumberReader lnrdr = new LineNumberReader(frdr);
			String lineBuffer = null;
			while ((lineBuffer = lnrdr.readLine()) != null) {
				String[] columns = Helpers.parseLine(lineBuffer);
				int cnt = 0;
				for (String column : columns) {
					fw.write((++cnt > 1 ? "," : "") + Helpers.formatCsvField(column));
				}
				fw.write(System.lineSeparator());
			}
			fw.flush();
			fw.close();
			frdr.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String fileToString(String fileNm, String lineEndString, String ignoreAfterStr) throws IOException {
		StringBuffer sb = new StringBuffer();
		FileReader frdr = new FileReader(fileNm);
		LineNumberReader lnrdr = new LineNumberReader(frdr);
		String lineBuffer = null;
		while ((lineBuffer = lnrdr.readLine()) != null) {
			int commentOffset = -1;
			if (ignoreAfterStr != null)
				commentOffset = lineBuffer.indexOf(ignoreAfterStr);
			if (commentOffset != 0) {
				if (commentOffset > 0)
					lineBuffer = lineBuffer.substring(0, commentOffset);
				sb.append(lineBuffer).append(lineEndString == null ? "" : lineEndString);
			}
		}
		return sb.toString();
	}
	
	public static List<String> fileToSqlList(String fileNm) throws IOException {
		return fileToSqlList(fileNm, null, "--");
	}
	
	public static List<String> fileToSqlList(String fileNm, String lineEndString, String ignoreAfterStr) throws IOException {
		StringBuffer sb = new StringBuffer();
		FileReader frdr = new FileReader(fileNm);
		LineNumberReader lnrdr = new LineNumberReader(frdr);
		String lineBuffer = null;
		List<String> sqlStatments = new ArrayList<>();
		while ((lineBuffer = lnrdr.readLine()) != null) {
			if (lineBuffer.trim().length()==0) continue;
			int commentOffset = -1;
			if (ignoreAfterStr != null)
				commentOffset = lineBuffer.indexOf(ignoreAfterStr);
			if (commentOffset != 0) {
				if (commentOffset > 0)
					lineBuffer = lineBuffer.substring(0, commentOffset);
				sb.append(lineBuffer).append(lineEndString == null ? " " : lineEndString);
				if (lineBuffer.contains(";")) {
					sqlStatments.add(sb.toString().replace(";", "").trim());
					sb = new StringBuffer();
				}
			}
		}
		return sqlStatments;
	}

	public static int getMaxRowLength(String fileName) {
		int maxRowLength = 0;
		try {
			FileInputStream fis = new FileInputStream(fileName);

			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() > maxRowLength)
					maxRowLength = line.length();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return maxRowLength;
	}

	public static int getRecordCount(String fileName) {
		int recordCnt = 0;
		try {
			FileInputStream fis = new FileInputStream(fileName);

			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			while ((line = br.readLine()) != null) {
				recordCnt++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return recordCnt;
	}

	public static boolean existsBoolean(String filename) {
		File f = new File(filename);
		if (!f.exists())
			return false;
		else
			return true;
	}

	public static int pathType(String filename) {
		File f = new File(filename);
		if (!f.exists()) {
			return TYPE_INVALID;
		} else {
			if (f.isFile())
				return TYPE_FILE;
			else if (f.isDirectory())
				return TYPE_DIRECTORY;
			else
				return TYPE_INVALID;
		}
	}

	public static void exists(String filename) {
		File f = new File(filename);
		if (!f.exists())
			failArgument("No such file or directory (" + filename + ") exists.");
	}

	public static boolean isFile(String filename) {
		File f = new File(filename);
		if (f == null)
			return false;
		return f.isFile();
	}

	public static boolean isDirectory(String filename) {
		File f = new File(filename);
		if (f == null)
			return false;
		return f.isDirectory();
	}

	public static void delete(String filename) {
		// create a file object to represent the file name
		File f = new File(filename);
		// f.renameTo(

		// make sure the file or director exists and is not write protected
		if (!f.exists())
			failArgument("Delete: No such file or directory (" + filename + ") exists.");
		if (!f.canWrite())
			failArgument("Delete: File or directory (" + filename + ") is write protected.");

		// if it is a directory insure that it is empty
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0)
				failArgument("Delete: Directory (" + filename + ") is not empty.");
		}

		// if we passed all the test attempt to delete.
		boolean successful = f.delete();
		if (!successful)
			failArgument("Delete: deletion of " + filename + " failed.");

	}

	public static void copy(String fromName, String toName) throws IOException {
		File fromFile = new File(fromName);
		File toFile = new File(toName);

		// Make sure the source file exists and is readable
		if (!fromFile.exists())
			failIO("Copy: No such file (" + fromName + ") exists.");
		if (!fromFile.isFile())
			failIO("Copy: can not copy directory (" + fromName + ").");
		if (!fromFile.canRead())
			failIO("Copy: Source file (" + fromName + ") is unreadable.");

		// If the destination is a directory, use the source file name as the
		// destination file name
		if (toFile.isDirectory())
			toFile = new File(toFile, fromFile.getName());

		// If the destination exists make sure it is a writable file and ask
		// before over writing
		if (toFile.exists()) {
			if (!toFile.canWrite())
				failIO("Copy: Source file (" + toName + ") is unwritable.");
			// add something for a message box in future, if frame is passed
			// have overloaded copy function, one with 3rd parameter being frame
		} else {
			// if file doesn't exist, check if directory exists and is writable.
			// If getParent() returns null, then the direcotry is the current
			// directory
			// so we can look up the user.dir system property to find out what
			// is.
			String parent = toFile.getParent(); // get the destination directory
			if (parent == null)
				parent = System.getProperty("user.dir"); // or CWD
			File dir = new File(parent); // convert the directory to file
			// Make sure the target file exists and is readable
			if (!dir.exists())
				failIO("Copy: Destination directory " + parent + ") does not exist.");
			if (dir.isFile())
				failIO("Copy: Destination is not a directory (" + parent + ").");
			if (!dir.canWrite())
				failIO("Copy: Destination directory (" + fromName + ") is unwriteable.");
		}

		// If everything checks out .. we copy the files one at a time
		FileInputStream from = null; // Stream to read from source
		FileOutputStream to = null; // Stream to write to destination

		try {
			from = new FileInputStream(fromFile); // Create an input stream
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096]; // A buffer to hold file contents
			int bytesRead; // store how many bytes in buffer

			// Read a chunck of bytes into the buffer, then write them out,
			// looping until
			// we reach the end of the file (when read() returns -1).
			while ((bytesRead = from.read(buffer)) != -1)
				// read bytes until EOF
				to.write(buffer, 0, bytesRead); // write bytes
		}
		// Always close the streams, even if exceptions were thrown
		finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					;
				}
		}
	}

	public static String toString(String fileName) throws IOException {
		File file = new File(fileName);

		// Make sure the source file exists and is readable
		if (!file.exists())
			failIO("toString: No such file (" + fileName + ") exists.");
		if (!file.isFile())
			failIO("toString: can not copy directory (" + fileName + ").");
		if (!file.canRead())
			failIO("toString: Source file (" + fileName + ") is unreadable.");

		/*
		 * // Construct BufferedReader from FileReader BufferedReader br = new
		 * BufferedReader(new FileReader(file)); StringBuffer out = new StringBuffer();
		 * 
		 * String line = null; while ((line = br.readLine()) != null) {
		 * //System.out.println(line); out.append(line); } br.close();
		 */
		FileInputStream fis = new FileInputStream(fileName);
		StringBuffer out = new StringBuffer();

		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		while ((line = br.readLine()) != null) {
			// System.out.println(line);
			out.append(line).append("\n");
		}

		br.close();
		return out.toString();
	}

	public static void rename(String fromName, String toName) throws IOException {
		File fromFile = new File(fromName);
		File toFile = new File(toName);

		// Make sure the source file exists and is readable
		if (!fromFile.exists())
			failIO("Rename: No such file (" + fromName + ") exists.");
		if (!fromFile.isFile())
			failIO("Rename: can not rename directory (" + fromName + ").");
		if (toFile.isDirectory())
			failIO("Rename: To name must be a file, not a directory (" + fromName + ").");

		// if file doesn't exist, check if directory exists and is writable.
		// If getParent() returns null, then the direcotry is the current
		// directory
		// so we can look up the user.dir system property to find out what is.
		String toParent = toFile.getParent(); // get the destination directory
		String fromParent = fromFile.getParent(); // get the source directory

		if (toParent == null) {
			if (fromParent == null)
				toParent = System.getProperty("user.dir"); // or CWD
			else
				toParent = fromParent;
		}

		File fullToFileName = new File(toParent + "\\" + toFile.getName()); // convert
																			// the
																			// directory
																			// to
																			// file
		if (fullToFileName.exists())
			failIO("Rename: Rename file name already exists (" + fullToFileName + ").");

		if (!fromFile.renameTo(fullToFileName))
			failIO("Rename: Rename failed (" + fromName + " -> " + toName + ").");
	}

	public static boolean makeDirectory(String dirName) {
		return makeDirectory(dirName, "/");
	}

	public static boolean makeDirectory(String dirName, String separator) {

		// File f = new File(dirName);
		// if (!f.isDirectory()) {
		// if (!f.mkdir()) return false;
		// }
		// return true;
		String token = "";
		String dir = "";
		int tokenCount = 0;
		StringTokenizer st = new StringTokenizer(dirName, separator);

		while (st.hasMoreTokens()) {
			token = st.nextToken().trim();
			if (tokenCount > 0)
				dir += separator;
			dir += token;

			File f = new File(dir);
			if (!f.isDirectory()) {
				if (!f.mkdir())
					return false;
			}
			tokenCount++;
		}
		return true;
	}

	/**
	 * Given a URL path string, this will return the reverse path. For example, if
	 * the URL path string is "java/lang" the method will return URL specific string
	 * "../".
	 */
	public static String getBackPath(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		StringBuffer backpath = new StringBuffer();
		// int slashCount = 0;
		for (int i = 0; i < path.length(); i++) {
			char ch = path.charAt(i);
			if (ch == '\\') {
				// slashCount++;
				// if (slashCount == 2) {
				backpath.append("..");
				backpath.append(urlfileseparator);
				// }
				// else slashCount = 0;
			} // there is always a trailing fileseparator
		}
		return backpath.toString();
	}

	/** Convenience method to throw an exception */
	protected static void failArgument(String msg) throws IllegalArgumentException {
		throw new IllegalArgumentException(msg);
	}

	/** Convenience method to throw an exception */
	protected static void failIO(String msg) throws IOException {
		throw new IOException(msg);
	}

	// recursive method that walks directory
	// structure
	public static void getFileList(File f, List<String> list, String filter) {
		if (f.isDirectory()) {
			String entries[] = f.list();
			int maxlen = (entries == null ? 0 : entries.length);
			for (int i = 0; i < maxlen; i++) {
				getFileList(new File(f, entries[i]), list, filter);
			}
		} else if (f.isFile()) {
			if (filter != null) {
				Specifier s = new Specifier(filter);
				if (s.match(f.getName()))
					list.add(f.getPath());
			} else
				list.add(f.getPath());
		}
	}

	public static void getFileList(File f, List<String> list) {
		getFileList(f, list, null);
	}

	// top-level method to get list of path names
	// starting at a specified point in directory
	// structure. This path may contain wildcards
	// as used in directory calls
	public static List<String> getFileList(String fn) {

		// Get last qualifier to determine if wildcard was
		// used when called
		StringTokenizer st = new StringTokenizer(fn, File.separator);
		String lastQualifier = "";
		String filter = null;
		while (st.hasMoreTokens()) {
			lastQualifier = st.nextToken();
		}
		// if a wildcard has been used set the filter
		if (lastQualifier.indexOf("*") > -1) {
			filter = lastQualifier;
			if (fn.length() == lastQualifier.length())
				fn = System.getProperty("user.dir") + File.separator;
			else
				fn = fn.substring(0, fn.length() - lastQualifier.length());
		}

		List<String> list = new ArrayList<String>();
		getFileList(new File(fn), list, filter);
		return list;
	}

	public static ArrayList<String[]> getRowsFromDelimitedFile(String fileName, String delimiter) {
		ArrayList<String[]> rows = null;

		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);

			StringBuffer out = new StringBuffer();

			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			int rowCnt = 0;
			while ((line = br.readLine()) != null) {
				// String[] columns = line.split(delimiter);
				/*
				 * The following regular expression matches commas that are followed by an even
				 * number of quotes (or no quotes). Comma's inside quotes (i.e. the ones we
				 * don't want to match/split on) should have an odd number of quotes between
				 * them and the end of the line.
				 */
				String[] columns = line.split(delimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				for (int i = 0; i < columns.length; i++) {
					// columns[i] = columns[i].replaceAll("^\"|\"$", "");
					String temp = columns[i];
					// String temp2 = temp.replaceAll("^\"|\"$", "");
					String temp3 = temp.replaceAll("\"", "");
					columns[i] = temp3;
				}

				if (rows == null)
					rows = new ArrayList<String[]>();
				rows.add(columns);
			}

			br.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return rows;
	}

	public static ArrayList<Object> getObjectsFromDelimitedFile(String fileName, String targetClassName,
			String delimiter) {
		ArrayList<Object> rows = null;

		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);

			StringBuffer out = new StringBuffer();

			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			int rowCnt = 0;
			String[] columns = null;
			while ((line = br.readLine()) != null) {
				if (++rowCnt == 1)
					// columns = line.split(delimiter);
					columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				else {
					// String[] values = line.split(delimiter);
					String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

					if (rows == null)
						rows = new ArrayList<Object>();
					rows.add(DataMarshaller.getObjectFromArray(columns, values, targetClassName));
				}
			}

			System.out.println("Rows processed: " + rowCnt);
			br.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return rows;
	}

}
