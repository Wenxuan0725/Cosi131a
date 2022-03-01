/**
* This is a class initialize and process all filters
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;

import java.util.*;
import java.util.List;

import cs131.pa1.filter.Message;

/**
 * This class manages the parsing and execution of a command. It splits the raw
 * input into separated subcommands, creates subcommand filters, and links them
 * into a list.
 * 
 * @author cs131a
 *
 */
public class SequentialCommandBuilder {
	/**
	 * There are 7 fields in this class
	 * SequentialFilter last contains the last filter
	 * int count counts the number of filters has already initialized
	 * String[] subcommand is an array contains all filters except the last one 
	 * List res is an ArrayList which contains all filters 
	 * all is an arrayList contains all filters
	 * needInput is an arrayList contains all filters which need input
	 * haveOutput is an arrayList contains all  filters which have output 
	 */
	static SequentialFilter last;
	static int count=1;
	static String[] subcommand;
	static List<SequentialFilter> res=new ArrayList<>();
	static ArrayList<String> all=new ArrayList<>(Arrays.asList("cd","pwd","ls","cat","grep","wc","uniq","head","tail",">"));
	static ArrayList<String> needInput=new ArrayList<>(Arrays.asList("grep","wc","uniq","head","tail",">"));
	static ArrayList<String> haveOutPut=new ArrayList<>(Arrays.asList("pwd","ls","cat","grep","wc","uniq","head","tail"));
	/**
	 * Creates and returns a list of filters from the specified command
	 * 
	 * @param command the command to create filters from
	 * @return the list of SequentialFilter that represent the specified command
	 * @throws Exception 
	 */
	public static List<SequentialFilter> createFiltersFromCommand(String command) {
		if(count!=1) {
			count=1;
		}
		subcommand=command.split("\\|");
		for(int i=0;i<subcommand.length;i++) {
			subcommand[i]=subcommand[i].trim();
		}
		if(!all.contains(subcommand[0].split(" ")[0])) {
			System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(subcommand[0]));
			return null;
		}
		if(needInput.contains(subcommand[0].split(" ")[0])) {
			System.out.print(Message.REQUIRES_INPUT.with_parameter(subcommand[0]));
			return null;
		}
		SequentialFilter current=constructFilterFromSubCommand(subcommand[0]);
		if(res!=null) {
			res=new ArrayList<>();
		}
		current.setPrevFilter(current);
		if(current.processLine("").equals("False")) {
			return null;
		}
		current.process();
		if(current.processLine("").equals("False")) {
			return null;
		}
		res.add(current);
		for(int i=1;i<subcommand.length;i++) {
			if(!linkFilters(res)) {
				return null;
			}
			
		}
		
		return res;
	}

	/**
	 * Returns the filter that appears last in the specified command
	 * 
	 * @param command the command to search from
	 * @return the SequentialFilter that appears last in the specified command
	 * @throws Exception 
	 */
	private static SequentialFilter determineFinalFilter(String command) {
		return constructFilterFromSubCommand(command);
	}

	/**
	 * Returns a string that contains the specified command without the final filter
	 * 
	 * @param command the command to parse and remove the final filter from
	 * @return the adjusted command that does not contain the final filter
	 * @throws Exception 
	 */
	private static String adjustCommandToRemoveFinalFilter(String command) {
		if(command.indexOf("|")==-1) {
			last=determineFinalFilter(command);
			return command;
		}
		for(int i=command.length()-1;i>=0;i--) {
			if(command.substring(i,i+1).equals("|")) {
				last=determineFinalFilter(command.substring(i+1));
				return command.substring(0,i);
			}
		}
		return null;//impossible to reach
		
	}

	/**
	 * Creates a single filter from the specified subCommand
	 * 
	 * @param subCommand the command to create a filter from
	 * @return the SequentialFilter created from the given subCommand
	 * @throws Exception 
	 */
	private static SequentialFilter constructFilterFromSubCommand(String subCommand) {
		
		String[] data=subCommand.trim().split(" ")	;
		if(data[0].equals("cat")) {
			Cat filter=new Cat(subCommand);
			return filter;
		}else if(data[0].equals("cd")) {
			Cd filter=new Cd(subCommand);
			return filter;
		}else if(data[0].equals("grep")) {
			Grep filter=new Grep(subCommand);
			return filter;
		}else if(data[0].equals("head")) {
			Head filter=new Head(subCommand);
			return filter;
		}else if(data[0].equals("ls")) {
			ls filter=new ls(subCommand);
			return filter;
		}else if(data[0].equals("pwd")) {
			Pwd filter=new Pwd(subCommand);
			return filter;
		}else if(data[0].equals("tail")) {
			Tail filter=new Tail(subCommand);
			return filter;
		}else if(data[0].equals("uniq")) {
			Uniq filter=new Uniq(subCommand);
			return filter;
		}else if(data[0].equals("wc")) {
			Wc filter=new Wc(subCommand);
			return filter;
		}else {
			System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(subCommand));
			return null;
		}
		
		
	}

	/**
	 * links the given filters with the order they appear in the list
	 * 
	 * @param filters the given filters to link
	 * @return true if the link was successful, false if there were errors
	 *         encountered. Any error should be displayed by using the Message enum.
	 * @throws Exception 
	 */
	private static boolean linkFilters(List<SequentialFilter> filters) {
		if(!all.contains(subcommand[count].split(" ")[0])){
			System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(subcommand[count]));
			return false;
		}
		if(haveOutPut.contains(subcommand[count-1].split(" ")[0])&&!needInput.contains(subcommand[count].split(" ")[0])) {
			System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(subcommand[count]));
			return false;
		}else if(!haveOutPut.contains(subcommand[count-1].split(" ")[0])&&needInput.contains(subcommand[count].split(" ")[0])) {
			System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(subcommand[count-1]));
			return false;
		}else if (subcommand[count-1].indexOf(">")!=-1&&needInput.contains(subcommand[count].split(" ")[0])){
			System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(subcommand[count-1].substring(subcommand[count-1].indexOf(">"))));
			return false;
		}else {
			SequentialFilter current=constructFilterFromSubCommand(subcommand[count]);
			if(current.processLine("").equals("False")) {
				return false;
			}
			res.add(current);
			res.get(count).setPrevFilter(res.get(count));
			res.get(count-1).setNextFilter(res.get(count));
			current.process();
			if(current.processLine("").equals("False")) {
				return false;
			}
			count++;
			return true;
		}
	}
}
