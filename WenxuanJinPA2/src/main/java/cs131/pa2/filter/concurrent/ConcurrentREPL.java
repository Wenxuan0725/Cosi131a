/**
* This is the main class to run the project
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/15/2021
* COSI 131A PA1
*/
package cs131.pa2.filter.concurrent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import cs131.pa2.filter.Message;

/**
 * The main implementation of the REPL loop (read-eval-print loop). It reads
 * commands from the user, parses them, executes them and displays the result.
 * 
 * @author cs131a
 *
 */
public class ConcurrentREPL {
	/**
	 * the path of the current working directory
	 */
	static String currentWorkingDirectory;

	/**
	 * pipe string
	 */
	static final String PIPE = "|";

	/**
	 * redirect string
	 */
	static final String REDIRECT = ">";
	
	static LinkedHashMap<String,ArrayList<Thread>> bgThreads = new LinkedHashMap<>();
	static LinkedHashMap<String,Integer> record1=new LinkedHashMap<>();
	
	/**
	 * This is the method to deal with condition when the command line input by the user contains "&"
	 * @param cmd the command line user input
	 */
	public static void bgThread(String cmd) {
		cmd=cmd.substring(0,cmd.length()-2);
		List<ConcurrentFilter> filters = ConcurrentCommandBuilder.createFiltersFromCommand(cmd);
		ArrayList<Thread> curr=new ArrayList<>();
		for (ConcurrentFilter filter : filters) {
			Thread temp=new Thread(filter);
			curr.add(temp);
			temp.start();
		}
		bgThreads.put(cmd, curr);
	}
	
	/**
	 * This is a method to display all thread that are currently working in background.
	 */
	public static void repl() {
		int count=0;
		ArrayList<String> delete=new ArrayList<>();
		record1=new LinkedHashMap<>();
		for(String key: bgThreads.keySet()) {
			boolean check=false;
			
				for(int j=0;j<bgThreads.get(key).size();j++) {
					if(bgThreads.get(key).get(j).isAlive()) {
						check=true;
						count++;
						String current="\t"+count+". "+key+" &";
						System.out.println(current);
						record1.put(key,count);
						break;
					}
				}
				if(bgThreads.get(key).size()==0) {
					count++;
				}
				if(check==false) {
					delete.add(key);
				}
		}
		for(int i=0;i<delete.size();i++) {
			bgThreads.put(delete.get(i),new ArrayList<>());
			record1.put(delete.get(i),-1);
		}
	}
	
	/**
	 * This is a method to kill a thread in background.
	 * @param cmd the command user input
	 */
	public static void kill(String cmd) {
		int position=cmd.indexOf(" ");
		//Scanner temp=new Scanner(cmd);
		int index=Integer.parseInt(cmd.substring(position+1));
		if(record1.isEmpty()) {
			int count=1;
			for(String bg:bgThreads.keySet()) {
				record1.put(bg,count);
				count++;
			}
		}
		for(String key: bgThreads.keySet()) {
			if(index==record1.get(key)) {
				for(Thread thread:bgThreads.get(key)) {
					if(thread.isAlive()) {
						thread.interrupt();
						break;
					}
				}
				if(key.contains(REDIRECT)) {
					int redirect=key.indexOf(REDIRECT);
					String location=key.substring(redirect+2);
					File file=new File(location);
					if(file.exists()) {
						file.delete();
					}
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				bgThreads.put(key,new ArrayList<>());
				record1.put(key,-1);
				return;
			}
		}
	}
	
	/**
	 * The main method that will execute the REPL loop
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {

		// set cwd here so that it can be reset by tests that run main() function
		currentWorkingDirectory = System.getProperty("user.dir");

		Scanner consoleReader = new Scanner(System.in);
		bgThreads=new LinkedHashMap<>();
		record1=new LinkedHashMap<>();
		System.out.print(Message.WELCOME);
		


		// whether or not shell is running
		boolean running = true;

		do {
			List<Thread> record=new ArrayList<Thread>();
			System.out.print(Message.NEWCOMMAND);

			// read user command, if its just whitespace, skip to next command
			String cmd = consoleReader.nextLine();
			if (cmd.isBlank()) {
				continue;
			}

			try {
				//check if the command contains "&", "kill" or "repl_jobs"
				if(cmd.endsWith("&")) {
					bgThread(cmd);
				}else if(cmd.startsWith("kill")) {
					kill(cmd);
				}else if(cmd.equals("repl_jobs")) {
					repl();
				}else {
					// parse command into sub commands, then into Filters, add final PrintFilter if
					// necessary, and link them together - this can throw IAE so surround in
					// try-catch so appropriate Message is printed (will be the message of the IAE)
					List<ConcurrentFilter> filters = ConcurrentCommandBuilder.createFiltersFromCommand(cmd);

					// if we have only an ExitFilter, that means user typed "exit" or "exit"
					// surrounded by ws, stop the shell
					if (filters.size() == 1 && filters.get(0) instanceof ExitFilter) {
						running = false;
							try {
								for(String key: bgThreads.keySet()) {
									for(int i=0;i<bgThreads.get(key).size();i++) {
										 bgThreads.get(key).get(i).join();
									}
								}
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} else {
						// otherwise, call process on each of the filters to have them execute
						for (ConcurrentFilter filter : filters) {
							Thread temp=new Thread(filter);
							record.add(temp);
						}
						if(!record.isEmpty()) {
							try {
								for(int i=0;i<record.size();i++) {
									record.get(i).start();
								}
								for(int i=0;i<record.size();i++) {
									record.get(i).join();
								}
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						}
					}
				
				}
				
			} catch (IllegalArgumentException e) {
				System.out.print(e.getMessage());
			}

		} while (running);
		System.out.print(Message.GOODBYE);

		consoleReader.close();

	}

}
