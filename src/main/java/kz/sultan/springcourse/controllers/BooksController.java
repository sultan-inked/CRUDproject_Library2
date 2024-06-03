package kz.sultan.springcourse.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
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
import kz.sultan.springcourse.models.Book;
import kz.sultan.springcourse.models.Person;
import kz.sultan.springcourse.services.BooksService;
import kz.sultan.springcourse.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BooksController {
	private final BooksService booksService;
	private final PeopleService peopleService;
	
	@Autowired
	public BooksController(BooksService booksService, PeopleService peopleService) {
		this.booksService = booksService;
		this.peopleService = peopleService;
	}
	
	
	
//	@GetMapping()
//	public String index(Model model) {
//		model.addAttribute("books", booksService.findAll());
//		return "books/index";
//	}
	
	@GetMapping("/date")
	public String date() {
		// Take cuurent date ans time
		Instant now = Instant.now();
		
		// Book creation time
		Date bookCreatedAt = booksService.findOne(1).getCreatedAt();
		
		// Convert the creation date to an Instant object
		Instant bookCreatedAtInstant = bookCreatedAt.toInstant();
		
		// Calculate the time between the present moment and the moment of creation
		Duration duration = Duration.between(bookCreatedAtInstant, now);
		
		// Output the difference in milliseconds
		long ms = duration.toHours();
		System.out.println(ms);
		return "redirect:/books?page=0&size=5&sort_by_year=false";
	}
	
	@GetMapping()
	public String listBooks(Model model,
							@RequestParam("page") int page,
							@RequestParam("size") int size,
							@RequestParam("sort_by_year") boolean sortByYear) {
		List<Book> books;
		if(sortByYear) {
			books = booksService.getBooksSortedByYearWithPagination(page, size);
		} else {
			books = booksService.getBooksPerPage(page, size);
		}
		Integer totalPages = booksService.findAll().size() / size;
		
		model.addAttribute("books", books);
		model.addAttribute("total_pages", totalPages);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("desired_book", new Book());
		model.addAttribute("sort_by_year", sortByYear);
		
		return "books/list";
	}
	
	@GetMapping("/search")
	public String search(@ModelAttribute("desired_book") Book desiredBook, Model model) {
		model.addAttribute("list_of_desired_book", booksService
				.findBookByNameOrAuthorPrefixIgnoreCase(desiredBook));
		return "books/search";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model,
			@ModelAttribute("desired_person") Person desiredPerson) {
		var book = booksService.findOne(id);
		model.addAttribute("book", book);
		if(book.getOwner() == null) {
			List<Person> personAsOwnerList = peopleService.
					findPersonByFirstNameOrLastNamePrefixIgnoreCase(desiredPerson);
			if(personAsOwnerList.size() != 0) {
				model.addAttribute("people", personAsOwnerList);
			} else {
				model.addAttribute("people", peopleService.findAll());
			}
			model.addAttribute("person", new Person());
			// model.addAttribute("desired_person", new Person());
		}else {
			model.addAttribute("person", book.getOwner());
		}
		return "books/show";
	}
	
	@GetMapping("/new")
	public String newBook(@ModelAttribute("book") Book book) {
		return "books/new";
	}
	
	@PostMapping()
	public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "books/new";
		}
		booksService.save(book);
		return "redirect:/books?page=0&size=5&sort_by_year=false";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("book", booksService.findOne(id));
		return "books/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
			@PathVariable("id") int id) {
		if(bindingResult.hasErrors()) {
			return "books/edit";
		}
		Book existingBook = booksService.findOne(id);
		book.setCreatedAt(existingBook.getCreatedAt());
		booksService.update(id, book);
		return "redirect:/books?page=0&size=5&sort_by_year=false";
	}
	
	@PatchMapping("/{id}/assign")
	public String assign(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
		booksService.assign(person, booksService.findOne(id));
		return "redirect:/books/" + id;
	}
	
	@PatchMapping("/{id}/release")
	public String release(@PathVariable("id") int id) {
		booksService.release(booksService.findOne(id));
		return "redirect:/books/" + id;
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		booksService.delete(id);
		return "redirect:/books?page=0&size=5&sort_by_year=false";
	}
}
