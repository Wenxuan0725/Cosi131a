/**
* This is a class initialize the tail
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;


import java.util.*;

import cs131.pa1.filter.Message;

public class Tail extends SequentialFilter{
	
	/**
	 * There are 4 fields in the class
	 * String[] data is a array which contains information split from command
	 * int larger is a signal to record whether the command contains redirection
	 * larger test is a sequential filter which will be initialize when there is a redirection
	 * boolean get represents whether it meets error during initializing and processing
	 */
	String[] data;
	int larger=-1;
	larger test;
	boolean get=true;
	
	/**
	 * This is the constructor of the class
	 * @param command is a String which entered by user
	 */
	public Tail(String command)  {
		super();
		if(command.indexOf(">")!=larger) {
			larger=command.indexOf(">");
			test=new larger(command);
			if(test.processLine("").equals("False")) {
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
		Queue<String> lastTen=new LinkedList<>();
		while(!input.isEmpty()) {
			String temp=input.poll();
			if(temp!=null) {
				lastTen.add(temp);
			}
			
			if(lastTen.size()>10) {
				lastTen.poll();
			}
		}
		int index;
		if(lastTen.size()<10) {
			index=lastTen.size();
		}else {
			index=10;
		}
		for(int i=0;i<index;i++) {
			String current=lastTen.poll();
			if(larger!=-1) {
				test.store(current);
			}else {
				output.add(current);
			}
		}
		
		
	}
	
	
		
}
