/**
* This is a class initialize the wc
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

public class Wc extends SequentialFilter{
	/**
	 * There are 9 fields in the class
	 * String[] data is a array which contains information split from command
	 * String command is the command got from user input
	 * int line represents the number of lines of the input
	 * int word represents the number of words of the input
	 * int character represents the number of characters of the input
	 * int larger is a signal to record whether the command contains redirection
	 * larger test is a sequential filter which will be initialize when there is a redirection
	 * boolean get represents whether it meets error during initializing and processing
	 */
	String[] data;
	String command;
	Scanner file;
	int line;
	int word;
	int character;
	int larger=-1;
	larger test;
	boolean get=true;
	
	/**
	 * This is the constructor of the class
	 * @param command is a String which entered by user
	 */
	public Wc(String command) {
		this.command=command;
		if(command.indexOf(">")!=larger) {
			larger=command.indexOf(">");
			test=new larger(command);
			if(test.processLine("").equals("False")) {
				get=false;
				return;
			}
		}
		line=0;
		word=0;
		character=0;
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
	public void process(){
		if(input==null) {
			System.out.print(Message.REQUIRES_INPUT.with_parameter(command));
			get=false;
			return;
		}
		while(!input.isEmpty()) {
			String current=input.poll();
			line++;
			if(!current.equals(" ")) {
				String[]temp=current.split(" ");
				word+=temp.length;
				for(int i=0;i<temp.length;i++) {
					character+=temp[i].length();
				}
			}else {
				character++;
			}
			
		}
		if(larger!=-1) {
			test.store(""+line+" "+word+" "+character);
		}else {
			output.add(""+line+" "+word+" "+character);
		}
		
	}

	
}
