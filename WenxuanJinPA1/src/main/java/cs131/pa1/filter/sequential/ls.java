/**
* This is a class initialize the ls
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;

import java.io.File;


import cs131.pa1.filter.Message;

public class ls extends SequentialFilter{
	
	/**
	 * There are 3 fields in the class
	 * int larger is a signal to record whether the command contains redirection
	 * larger test is a sequential filter which will be initialize when there is a redirection
	 * boolean get represents whether it meets error during initializing and processing
	 */
	int larger=-1;
	larger test;
	boolean get=true;
	
	/**
	 * This is the constructor of the class
	 * @param command is a String which entered by user
	 */
	public ls(String command) {
		super();
		if(command.indexOf(">")!=larger) {
			larger=command.indexOf(">");
			test=new larger(command);
			if(test.processLine("").equals("False")) {
				get=false;
				return;
			}
		}
		if (input!=null) {
			System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter("ls"));
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
	 * This is a method override from Sequential Filter class to achieve the function of those command
	 */
	@Override
	public void process() {
		try {
			File file=new File(SequentialREPL.currentWorkingDirectory);
			File[] listFiles=file.listFiles();
			for(File current:listFiles) {
				
				if(larger!=-1) {
					test.store(current.getName());
				}else {
					output.add(current.getName());
				}
			}
			
		}
		catch(Exception e) {
			System.out.print(Message.DIRECTORY_NOT_FOUND.with_parameter("ls"));
			get=false;
			return;
		}
		
		
		
	}
	
	
	
	
}
