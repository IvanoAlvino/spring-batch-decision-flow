package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor2 implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toLowerCase();

        final Person transformedPerson = new Person(firstName, person.getLastName());

        log.info("Using Ivano processing to Convert (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}