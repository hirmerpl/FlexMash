package de.unistuttgart.ipvs.as.flexmash.BPELMappings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * A class to manage the BPEL syntax with .txt files within the project
 * 
 * @author mahrous
 *
 */
public class BPELMapper {

	/**
	 * 
	 * @param ServiceName
	 * @return BPEL syntax
	 * @throws IOException
	 */
	public  static String getBPELConfig(String ServiceName) throws IOException{
		
		String filepath ="files/BPELTemplates/"+ServiceName.toLowerCase()+"_template.txt";
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath));) 
		{	    
		    String line = br.readLine();
		    
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		}
		return sb.toString();
	}
}
