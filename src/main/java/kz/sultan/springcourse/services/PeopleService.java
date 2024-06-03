package kz.sultan.springcourse.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kz.sultan.springcourse.models.Person;
import kz.sultan.springcourse.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {
	private final PeopleRepository peopleRepository;
	
	@Autowired
	public PeopleService(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}
	
	
	
	public List<Person> findAll() {
		return peopleRepository.findAll();
	}
	
	public List<Person> findPersonByFirstNameOrLastNamePrefixIgnoreCase(Person desiredPerson) {
		String firstNamePrefix = desiredPerson.getFirstName(),
				lastNamePrefix = desiredPerson.getLastName();
		if(firstNamePrefix == null && lastNamePrefix == null) {
			return List.of();
		} else if(!firstNamePrefix.isEmpty() && !lastNamePrefix.isEmpty()) {
			return peopleRepository.findByFirstNameStartingWithIgnoreCaseOrLastNameStartingWithIgnoreCase(
					firstNamePrefix, lastNamePrefix);
		} else if(!firstNamePrefix.isEmpty() && lastNamePrefix.isEmpty()) {
			return peopleRepository.findByFirstNameStartingWithIgnoreCase(firstNamePrefix);
		}else if(firstNamePrefix.isEmpty() && !lastNamePrefix.isEmpty()) {
			return peopleRepository.findByLastNameStartingWithIgnoreCase(lastNamePrefix);
		} else {
			return List.of();
		}
	}
	
	public List<Person> getPeople(int page, int itemsPerPage) {
		Pageable pageable = PageRequest.of(page, itemsPerPage);
		Page<Person> peoplePage = peopleRepository.findAll(pageable);
		return peoplePage.getContent();
	}
	
	public Person findOne(int id) {
		Optional<Person> foundPerson = peopleRepository.findById(id);
		return foundPerson.orElse(null);
	}
	
	public Person findByEmail(String email) {
		Optional<Person> foundPerson = peopleRepository.findByEmail(email).stream().findAny();
		return foundPerson.orElse(null);
	}
	
	@Transactional
	public void save(Person person) {
		person.setCreatedAt(new Date());
		peopleRepository.save(person);
	}
	
	@Transactional
	public void update(int id, Person updatedPerson) {
		updatedPerson.setId(id);
		peopleRepository.save(updatedPerson);
	}
	
	@Transactional
	public void delete(int id) {
		peopleRepository.deleteById(id);
	}
}
