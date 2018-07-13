package hello;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemReader;

@Component("readerStep1")
public class NoOpItemReaderStep1 implements ItemReader<String>
{
	private static final Log log = LogFactory.getLog(NoOpItemReaderStep1.class);

	private int index = 0;

	/**
	 * Reads next record from input
	 */
	public String read() throws Exception {
		if (index < 1) {
			log.info("------------------------------------------");
			log.info("Inside step 1");
			log.info("------------------------------------------");
			index++;
			return "done";
		}
		else {
			return null;
		}
	}
}
