package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class PersonNameToLowerCaseProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonNameAndSurnameToUpperCaseProcessor.class);

    @Override
    public Person process(final Person person) {
        final String firstName = person.getFirstName().toLowerCase();

        final Person transformedPerson = new Person(firstName, person.getLastName());

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}