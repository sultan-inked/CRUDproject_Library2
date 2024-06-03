package kz.sultan.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kz.sultan.springcourse.models.Person;
import kz.sultan.springcourse.services.PeopleService;

@Component
public class PersonValidator implements Validator {
	
	private final PeopleService peopleService;

	@Autowired
	public PersonValidator(PeopleService peopleService) {
		this.peopleService = peopleService;
	}
	
	
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Person.class.equals(aClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person)target;
		
		if(peopleService.findByEmail(person.getEmail()) != null) {
			errors.rejectValue("email", "", "This email is already taken");
		}
	}
}
