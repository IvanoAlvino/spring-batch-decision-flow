package hello;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import java.util.List;

public class NoOpItemWriterStep2 implements ItemWriter<Object>
{
	private static final Log log = LogFactory.getLog(NoOpItemWriterStep2.class);

	public void write(List<? extends Object> data) throws Exception {
		log.info(data);
	}
}
