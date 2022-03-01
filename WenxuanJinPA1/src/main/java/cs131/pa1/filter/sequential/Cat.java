/**
* This is a class initialize the cat
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

public class Cat extends SequentialFilter{
	/**
	 * There are 4 fields in the class
	 * Scanner file is a scanner which contains information from the file name user input
	 * int larger is a signal to record whether the command contains redirection
	 * larger test is a sequential filter which will be initialize when there is a redirection
	 * boolean get represents whether it meets error during initializing and processing
	 */
	private Scanner file;
	int larger=-1;
	larger test;
	boolean get=true;
	
	/**
	 * This is the constructor of the class
	 * @param command is a String which entered by user
	 */
	public Cat(String command) {
		if(command.indexOf(">")!=larger) {
			larger=command.indexOf(">");
			test=new larger(command);
			if(test.processLine("").equals("False")) {
				get=false;
				return;
			}
		}
		if (input!=null) {
			System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(command));
			get=false;
			return;
		}
		String[] temp=command.split(" ");
		for(int i=0;i<temp.length;i++) {
			temp[i]=temp[i].trim();
		}
		if(temp.length<=1) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));
			get=false;
			return;
		}else {
			try {
				file=new Scanner(new File(SequentialREPL.currentWorkingDirectory+Filter.FILE_SEPARATOR+temp[1]));
			}
			catch(Exception e) {
				System.out.print(Message.FILE_NOT_FOUND.with_parameter(command));
				get=false;
				return;
			}
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
	 * This is a method override from Sequential Filter class to achieve the function of those command
	 */
	@Override
	public void process() {
		while(get==true&&file.hasNextLine()) {
			String current=file.nextLine();
			if(larger!=-1) {
				test.store(current);
			}else {
				output.add(current);
			}
		}
		
	}
	
	
	

}
