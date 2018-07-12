package hello;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /* Job definition **/
    /********************/
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step readFromCsvStep,
        Step readFromDbStep) {
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .start(readFromCsvStep)
            .next(readFromDbStep)
            .build();
    }

    /* Steps definitions **/
    /***********************/
    @Bean
    public Step readFromCsvStep(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("readFromCsvStep")
            .<Person, Person> chunk(10)
            .reader(personCsvReader())
            .processor(processor())
            .writer(writer)
            .build();
    }

    @Bean
    public Step readFromDbStep(ItemReader<Person> personDbReader, JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("readFromDbStep")
            .<Person, Person> chunk(10)
            .reader(personDbReader)
            .processor(processor2())
            .writer(writer)
            .build();
    }

    /* Step 1: read from CSV, output to database **/
    /***********************************************/
    @Bean
    public FlatFileItemReader<Person> personCsvReader() {
        return new FlatFileItemReaderBuilder<Person>()
            .name("personItemReader")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited()
            .names(new String[]{"firstName", "lastName"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }})
            .build();
    }

    @Bean
    public PersonNameAndSurnameToUpperCaseProcessor processor() {
        return new PersonNameAndSurnameToUpperCaseProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> databaseWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    /* Step 2: read from database, output to database **/
    /****************************************************/
    @Bean
    public ItemReader<Person> personDbReader(DataSource dataSource) {
        JdbcCursorItemReader<Person> dbReader = new JdbcCursorItemReader<>();

        dbReader.setDataSource(dataSource);
        dbReader.setSql("SELECT first_name, last_name FROM people");
        dbReader.setRowMapper(new BeanPropertyRowMapper<>(Person.class));

        return dbReader;
    }

    @Bean
    public PersonNameToLowerCaseProcessor processor2() {
        return new PersonNameToLowerCaseProcessor();
    }
}