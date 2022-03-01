/**
* This is the main class to run the project
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 10/4/2021
* COSI 131A PA1
*/
package cs131.pa1.filter.sequential;

import java.util.List;
import java.util.Scanner;

import cs131.pa1.filter.Message;

/**
 * The main implementation of the REPL loop (read-eval-print loop). It reads
 * commands from the user, parses them, executes them and displays the result.
 * 
 * @author cs131a
 *
 */
public class SequentialREPL {
	/**
	 * the path of the current working directory
	 */
	static String currentWorkingDirectory;

	/**
	 * The main method that will execute the REPL loop
	 * 
	 * @param args not used
	 * @throws Exception 
	 */
	public static void main(String[] args) {
		currentWorkingDirectory=System.getProperty("user.dir");
		System.out.print(Message.WELCOME);
		Scanner console=new Scanner(System.in);
		System.out.print(Message.NEWCOMMAND);
		while(console.hasNextLine()) {
			String current=console.nextLine();
			if(current.equals("exit")) {
				console.close();
				System.out.print(Message.GOODBYE);
				return;
			}else {
				List<SequentialFilter> data=SequentialCommandBuilder.createFiltersFromCommand(current);
//				for(int i=0;i<data.size();i++) {
//					data.get(i).process();
//				}
				while(data!=null&&!data.get(data.size()-1).output.isEmpty()) {
					String temp=data.get(data.size()-1).output.poll();
					System.out.println(temp);
				}
			}
			System.out.print(Message.NEWCOMMAND);
			
		}
	}

}
