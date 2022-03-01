/**
* This is a class initialize the cd
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;

import java.io.File;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

public class Cd extends SequentialFilter{
	
	/**
	 * There are 4 fields in the class
	 * String[] data is an array which contains information split from command
	 * String command is the command entered by user
	 * boolean get represents whether it meets error during initializing and processing
	 */
	private String[] data;
	private String command;
	boolean get=true;
	public Cd(String command) {
		super();
		if(input!=null) {
			System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(command));
			get=false;
			return;
		}
		data=command.split(" ");
		this.command=command;
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
		if(data.length==1) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));
			get=false;
			return;
		}else {
			if(data[1].equals(".")) {
				return;
			}else if(data[1].equals("..")) {
				for(int i=SequentialREPL.currentWorkingDirectory.length()-1;i>=0;i--) {
					if(SequentialREPL.currentWorkingDirectory.substring(i,i+1).equals(FILE_SEPARATOR)) {
						SequentialREPL.currentWorkingDirectory=SequentialREPL.currentWorkingDirectory.substring(0,i);
						break;
					}
				}
			}else {
				String temp=SequentialREPL.currentWorkingDirectory;
				temp+=FILE_SEPARATOR+data[1];
				File current=new File(temp);
				if(!current.isDirectory()) {
					System.out.print(Message.DIRECTORY_NOT_FOUND.with_parameter(command)); 
					get=false;
					return;
				}
				SequentialREPL.currentWorkingDirectory+=FILE_SEPARATOR+data[1];
				
			}
		}
	}
	

}
