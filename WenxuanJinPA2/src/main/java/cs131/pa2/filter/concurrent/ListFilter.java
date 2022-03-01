package cs131.pa2.filter.concurrent;

import java.io.File;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;

/**
 * Implements ls command - overrides necessary behavior of SequentialFilter
 * 
 * @author Chami Lamelas
 *
 */
public class ListFilter extends ConcurrentFilter {

	/**
	 * command that was used to construct this filter
	 */
	private String command;

	/**
	 * Constructs an ListFilter from an exit command
	 * 
	 * @param cmd - exit command, will be "ls" or "ls" surrounded by whitespace
	 */
	public ListFilter(String cmd) {
		super();
		command = cmd;
	}

	/**
	 * Overrides SequentialFilter.processLine() - doesn't do anything.
	 */
	@Override
	protected String processLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Overrides {@link ConcurrentFilter#process()} to add the files located in
	 * {@link ConcurrentREPL#currentWorkingDirectory} to the output queue.
	 */
	@Override
	public void process() {
		File cwd = new File(ConcurrentREPL.currentWorkingDirectory);
		File[] files = cwd.listFiles();
		for (File f : files) {
			try {
				this.output.put(f.getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//at the end, add a POSION_PILL
		try {
			output.put(POISON_PILL);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Overrides SequentialFilter.setPrevFilter() to not allow a
	 * {@link Filter} to be placed before {@link ListFilter} objects.
	 * 
	 * @throws IllegalArgumentException - always
	 */
	@Override
	public void setPrevFilter(Filter prevFilter) {
		throw new IllegalArgumentException(Message.CANNOT_HAVE_INPUT.with_parameter(command));
	}

}
