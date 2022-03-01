package cs131.pa2.filter.concurrent;

/**
 * Implements head command - overrides necessary behavior of SequentialFilter
 * 
 * @author Chami Lamelas
 *
 */
public class HeadFilter extends ConcurrentFilter {

	/**
	 * number of lines read so far
	 */
	private int numRead;

	/**
	 * number of lines passed to output via head
	 */
	private static int LIMIT = 10;

	/**
	 * Constructs a head filter.
	 */
	public HeadFilter() {
		super();
		numRead = 0;
	}

	/**
	 * Overrides {@link ConcurrentFilter#process()} to only add up to 10 lines to
	 * the output queue.
	 */
	@Override
	public void process() {
		while (!isDone() && numRead < LIMIT) {
			try {
				String line = input.take();
				if(!line.equals(POISON_PILL)) {
					output.put(line);
					numRead++;
				}else {
					break;
				}
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
	 * Overrides SequentialFilter.processLine() - doesn't do anything.
	 */
	@Override
	protected String processLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}
}
