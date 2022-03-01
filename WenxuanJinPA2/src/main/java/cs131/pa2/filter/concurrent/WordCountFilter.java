package cs131.pa2.filter.concurrent;

/**
 * Implements wc command - overrides necessary behavior of SequentialFilter
 * 
 * @author Chami Lamelas
 *
 */
public class WordCountFilter extends ConcurrentFilter {

	/**
	 * word count in input - words are strings separated by space in the input
	 */
	private int wordCount;

	/**
	 * character count in input - includes ws
	 */
	private int charCount;

	/**
	 * line count in input
	 */
	private int lineCount;

	/**
	 * Constructs a wc filter.
	 */
	public WordCountFilter() {
		super();
		wordCount = 0;
		charCount = 0;
		lineCount = 0;
	}

	/**
	 * Overrides {@link ConcurrentFilter#process()} by computing the word count,
	 * line count, and character count then adding the string with line count + " "
	 * + word count + " " + character count to the output queue
	 */
	@Override
	public void process() {
		while (!isDone()) {
			String line;
			try {
				line = input.take();
				if(line.equals(POISON_PILL)) {
					break;
				}
				processLine(line);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			output.put(lineCount + " " + wordCount + " " + charCount);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			output.put(POISON_PILL);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Overrides SequentialFilter.processLine() - updates the line, word, and
	 * character counts from the current input line
	 */
	@Override
	protected String processLine(String line) {
		lineCount++;
		wordCount += line.split(" ").length;
		charCount += line.length();

		// TODO Auto-generated method stub
		return null;
	}

}
