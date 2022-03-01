/**
* This is a class initialize the redirection
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

public class larger extends SequentialFilter{
	
	/**
	 * This class has 4 fields
	 * file is a new file created to contain input
	 * filWritter is used to write in the file
	 * boolean get represents whether it meets error during initializing and processing
	 * bufferWritter is a helper to write into a file
	 */
	File file;
	FileWriter fileWritter;
	boolean get=true;
	BufferedWriter bufferWritter;
	
	/**
	 * This is the constructor of the class
	 * @param command is a String which entered by user
	 */
	public larger(String command) {
		super();
		int index=command.indexOf(">");
		command=command.substring(index-1).trim();
		String[]data=command.split(" ");
		if(data.length<=1) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));
			get=false;
			return;
		}
		 file=new File(SequentialREPL.currentWorkingDirectory+Filter.FILE_SEPARATOR,data[data.length-1]);
		 try {
			 file.createNewFile();
		     fileWritter = new FileWriter(SequentialREPL.currentWorkingDirectory+Filter.FILE_SEPARATOR+data[data.length-1]);
		 }catch(Exception e) {
			 System.out.print(Message.FILE_NOT_FOUND.with_parameter(command));
			 get=false;
			 return;
		 }
	     
	}
	
	/**
	 * This is a method override from Sequential Filter class, which judge whether there is an error
	 */
	@Override
	protected String processLine(String line) {
		if(get) {
			return "True";
		}else {
			return "False";
		}
	}
	/**
	 * This is a method to achieve the function of those command
	 */
	public void store(String output){
        bufferWritter = new BufferedWriter(fileWritter);
        try {
			bufferWritter.write(output+"\n");
			bufferWritter.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
