package com.ibm.wfm.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileSortService {
	
	public FileSortService() {}
	
	public String sort(String fileName) {
		return sort(fileName, fileName);
	}
	
	public String sort(String fileName, String sortFileName) {
        FileReader fr = null;
		try {
			fr = new FileReader(fileName);

			BufferedReader reader = new BufferedReader(fr);
			ArrayList<String> str = new ArrayList<>();
			String line = "";
			while((line=reader.readLine())!=null){
					str.add(line);
			}
			reader.close();
			Collections.sort(str);
			FileWriter writer = new FileWriter(sortFileName);
			for(String s: str){
					writer.write(s);
					writer.write(System.lineSeparator());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        return fileName;
	}

}
