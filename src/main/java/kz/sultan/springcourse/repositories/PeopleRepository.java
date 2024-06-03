package kz.sultan.springcourse.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.sultan.springcourse.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
	
	List<Person> findByEmail(String email);
	
	List<Person> findByFirstNameStartingWithIgnoreCase(String prefix);
	List<Person> findByLastNameStartingWithIgnoreCase(String prefix);
	List<Person> findByFirstNameStartingWithIgnoreCaseOrLastNameStartingWithIgnoreCase(
			String FirstNamePrefix, String LastNamePrefix);
}
