package cs131.pa2.filter.concurrent;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa2.filter.Filter;

/**
 * An abstract class that extends the Filter and implements the basic functionality of all filters. Each filter should
 * extend this class and implement functionality that is specific for this filter. 
 * You should not modify this class.
 * @author cs131a
 *
 */
public abstract class ConcurrentFilter extends Filter implements Runnable{
	/**
	 * The input queue for this filter
	 */
	protected LinkedBlockingQueue<String> input;
	/**
	 * The output queue for this filter
	 */
	protected LinkedBlockingQueue<String> output;

	/**
	 * This POSION_PILL is a signal put at the end of each output
	 */
	final String POISON_PILL = "END";
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	/**
	 * Processes the input queue and passes the result to the output queue
	 */
	public void process() {
		while (!isDone()){//check if isDone
			try {
				String line = input.take();
				if(line.equals(POISON_PILL)){//if the line read from input is POSION_PILL, it means everything in input is read.
					break;
				}
				String processedLine = processLine(line);
				if (processedLine != null){
					output.put(processedLine);
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
		try {
			if(output != null) {
				output.put(POISON_PILL);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Override the run method in Thread.
	 */
	@Override
	public void run() {
		process();
	}

	/**
	 * This is a method to check if a process is done.
	 */
	@Override
	public boolean isDone() {
		return  input.size()==1 && input.peek().equals(POISON_PILL);
	}
	
	/**
	 * Called by the {@link #process()} method for every encountered line in the input queue.
	 * It then performs the processing specific for each filter and returns the result.
	 * Each filter inheriting from this class must implement its own version of processLine() to
	 * take care of the filter-specific processing.
	 * @param line the line got from the input queue
	 * @return the line after the filter-specific processing
	 */
	protected abstract String processLine(String line);
	
}
