package hello;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /* Job definition **/
    /*******************/
    @Bean
    public Job importJob() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");

        Flow flow =  flowBuilder
            .start(step1())
            .next(decision())
            .on(FlowDecision.COMPLETED)
            .to(step2())
            .from(decision())
            .on(FlowDecision.FAILED)
            .to(step3())
            .end();

        return jobBuilderFactory.get("importUserLoopJob")
            .incrementer(new RunIdIncrementer())
            .start(flow)
            .end()
            .build();
    }

    /* Steps definitions **/
    /**********************/
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .<String, String> chunk(10)
            .reader(readerStep1())
            .writer(writerStep1())
            .build();
    }


    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
            .<String, String> chunk(10)
            .reader(readerStep2())
            .writer(writerStep2())
            .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
            .tasklet(tasklet())
            .build();
    }

    /* Step 1: read and log **/
    /*************************/
    @Bean
    public NoOpItemReaderStep1 readerStep1(){
        return new NoOpItemReaderStep1();
    }

    @Bean
    public NoOpItemWriterStep1 writerStep1(){
        return new NoOpItemWriterStep1();
    }

    /* Step 2: read and log **/
    /*************************/
    @Bean
    public NoOpItemReaderStep2 readerStep2(){
        return new NoOpItemReaderStep2();
    }

    @Bean
    public NoOpItemWriterStep2 writerStep2(){
        return new NoOpItemWriterStep2();
    }

    /* Step 3: log and finish **/
    /***************************/
    @Bean
    public TaskletStep3 tasklet(){
        return new TaskletStep3();
    }

    /* Decision maker **/
    /*******************/
    @Bean
    public FlowDecision decision(){
        return new FlowDecision();
    }
}