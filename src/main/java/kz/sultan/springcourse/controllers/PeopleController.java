package kz.sultan.springcourse.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import kz.sultan.springcourse.models.Person;
import kz.sultan.springcourse.services.BooksService;
import kz.sultan.springcourse.services.PeopleService;
import kz.sultan.springcourse.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {
	private final PeopleService peopleService;
	private final BooksService booksService;
	private final PersonValidator personValidator;
	
	@Autowired
	public PeopleController(PeopleService peopleService, BooksService booksService, PersonValidator personValidator) {
		this.peopleService = peopleService;
		this.booksService = booksService;
		this.personValidator = personValidator;
	}
	
	
	
//	@GetMapping()
//	public String index(Model model) {
//		model.addAttribute("people", peopleService.findAll());
//		return "people/index";
//	}
	
	@GetMapping()
	public String listPeople(Model model,
							@RequestParam("page") int page,
							@RequestParam("size") int size) {
		List<Person> people = peopleService.getPeople(page, size);
		Integer totalPages = peopleService.findAll().size() / size;
		
		model.addAttribute("people", people);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		
		return "people/list";
	}
	
	@GetMapping("/search")
	public String search(@ModelAttribute("desired_person") Person desiredPerson, Model model) {
		System.out.println(desiredPerson);
		model.addAttribute("list_of_desired_person", peopleService
				.findPersonByFirstNameOrLastNamePrefixIgnoreCase(desiredPerson));
		return "people/search";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model) {
		Person person = peopleService.findOne(id);
		model.addAttribute("person", person);
		model.addAttribute("books", booksService.findAllByOwner(person));
		return "people/show";
	}
	
	@GetMapping("/new")
	public String newPerson(@ModelAttribute("person") Person person) {
		return "people/new";
	}
	
	@PostMapping()
	public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		//TODO: Create PersonValidator by Email
		personValidator.validate(person, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return "people/new";
		}
		peopleService.save(person);
		return "redirect:/people?page=0&size=5";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("person", peopleService.findOne(id));
		return "people/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
			@PathVariable("id") int id) {
		if(bindingResult.hasErrors()) {
			return "people/edit";
		}
		Person existingPerson = peopleService.findOne(id);
		person.setCreatedAt(existingPerson.getCreatedAt());
		peopleService.update(id, person);
		return "redirect:/people?page=0&size=5";
	}
	
	@PatchMapping("/{person_id}/release/{book_id}")
	public String release(@PathVariable("person_id") int personId, @PathVariable("book_id") int bookId) {
		booksService.release(booksService.findOne(bookId));
		return "redirect:/people/" + personId;
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		peopleService.delete(id);
		return "redirect:/people?page=0&size=5";
	}
}
