package person;

import com.github.javafaker.Faker;

import javax.persistence.*;
import java.time.ZoneId;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    public static void main(String[] args) {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for(int i=0; i<1000; i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        } finally {
            printPersons();
            em.close();
        }
    }

    private static Person randomPerson() {
        Person person = new Person();
        Address address = new Address();
        Faker faker = new Faker();

        person.setName(faker.name().fullName());
        person.setDob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        person.setGender(faker.options().option(Person.Gender.class));
        person.setAddress(address.builder()
                .country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .streetAddress(faker.address().streetAddress())
                .zip(faker.address().zipCode())
                .build());
        person.setEmail(faker.internet().emailAddress());
        person.setProfession(faker.company().profession());

        return person;
    }

    static private void printPersons() {

        EntityManager em = emf.createEntityManager();
        try {
            em.createQuery("SELECT p FROM Person p", Person.class).getResultList().forEach(System.out::println);
        } finally {
            em.close();
        }

    }

}
