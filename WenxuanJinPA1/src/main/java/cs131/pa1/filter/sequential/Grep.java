/**
* This is a class initialize the grep
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;

import java.util.Scanner;

import cs131.pa1.filter.Message;

public class Grep extends SequentialFilter{
	/**
	 * There are 4 fields in the class
	 * String[] data is an array which contains information split from command
	 * int larger is a signal to record whether the command contains redirection
	 * larger test is a sequential filter which will be initialize when there is a redirection
	 * boolean get represents whether it meets error during initializing and processing
	 */
	private String[] data;
	int larger=-1;
	larger test;
	boolean get=true;
	
	/**
	 * This is the constructor of the class
	 * @param command is a String which entered by user
	 */
	public Grep(String command) {
		if(command.indexOf(">")!=larger) {
			larger=command.indexOf(">");
			test=new larger(command);
			if(test.processLine("").equals("False")) {
				get=false;
				return;
			}
		}
		data=command.split(" ");
		if(data.length<=1) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));
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
	public void process(){
		while(get&&!input.isEmpty()) {
			String current=input.poll();
			if(current.indexOf(data[1])!=-1) {
				if(larger!=-1) {
					test.store(current);
				}else {
					output.add(current);
				}
			}
		}
		
	}
	

}
