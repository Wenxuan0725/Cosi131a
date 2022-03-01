/**
* This is a class initialize the uniq
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

public class Uniq extends SequentialFilter{
	
	/**
	 * There are 4 fields in the class
	 * String[] data is an array which contains information split from command
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
	public Uniq(String command) {
		super();
		if(command.indexOf(">")!=larger) {
			larger=command.indexOf(">");
			test=new larger(command);
			if(test.processLine("").equals("False")) {
				get=false;
				return;
			}
		}
		data=command.split(" ");
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
		ArrayList<String> record=new ArrayList<>();
		while(get&&!input.isEmpty()) {
			String current=input.poll();
			if(!record.contains(current)) {
				record.add(current);
				if(larger!=-1) {
					test.store(current);
				}else {
					output.add(current);
				}
			}
		}
		
		
	}
	
	
}
