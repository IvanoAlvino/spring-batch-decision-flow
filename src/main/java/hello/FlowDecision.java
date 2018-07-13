package hello;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import java.util.Random;

public class FlowDecision implements JobExecutionDecider
{
	private static final Log log = LogFactory.getLog(NoOpItemReaderStep1.class);

	public static final String COMPLETED = "COMPLETED";

	public static final String FAILED = "FAILED";

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution)
	{
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt();

		log.info("Executing Decision with randomInt = " + randomNumber);

		if (randomNumber % 2 == 0)
		{
			log.info("------------------------------------------");
			log.info("Completed -> go to step 2");
			log.info("------------------------------------------");
			return FlowExecutionStatus.COMPLETED;
		}

		log.info("------------------------------------------");
		log.info("Failed -> go to step 3");
		log.info("------------------------------------------");
		return FlowExecutionStatus.FAILED;
	}
}