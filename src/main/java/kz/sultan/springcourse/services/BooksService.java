package kz.sultan.springcourse.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kz.sultan.springcourse.models.Book;
import kz.sultan.springcourse.models.Person;
import kz.sultan.springcourse.repositories.BooksRepository;

@Service
@Transactional(readOnly = true)
public class BooksService {
	private final BooksRepository booksRepository;
	
	@Autowired
	public BooksService(BooksRepository booksRepository) {
		this.booksRepository = booksRepository;
	}
	
	
	
	public List<Book> findAll() {
		List<Book> books = booksRepository.findAll();
		for(Book book : books) {
			if(book.getAssignedAt() != null) {
				bookOverdueCheckSet(book);
			}
		}
		return books;
	}
	
	public List<Book> getBooksPerPage(int page, int itemsPerPage) {
		Pageable pageable = PageRequest.of(page, itemsPerPage);
		Page<Book> bookPage = booksRepository.findAll(pageable);
		
		List<Book> books = bookPage.getContent();
		for(Book book : books) {
			if(book.getAssignedAt() != null) {
				bookOverdueCheckSet(book);
			}
		}
		return bookPage.getContent();
	}
	
	public List<Book> getBooksSortedByYearWithPagination(int page, int itemsPerPage) {
		Pageable pageable = PageRequest.of(page, itemsPerPage, Sort.by("year"));
		Page<Book> bookPage = booksRepository.findAll(pageable);
		
		List<Book> books = bookPage.getContent();
		for(Book book : books) {
			if(book.getAssignedAt() != null) {
				bookOverdueCheckSet(book);
			}
		}
		return bookPage.getContent();
	}
	
	public List<Book> findAllByOwner(Person person) {
		
		List<Book> books =  booksRepository.findAllByOwner(person);
		for(Book book : books) {
			if(book.getAssignedAt() != null) {
				bookOverdueCheckSet(book);
			}
		}
		return booksRepository.findAllByOwner(person);
	}
	
//	public List<Book> findAllByName(String name) {
//		return booksRepository.findAllByName(name);
//	}
	
//	public List<Book> findBookByNamePrefix(String prefix) {
//		return booksRepository.findByNameStartingWithIgnoreCase(prefix);
//	}
	
	public List<Book> findBookByNameOrAuthorPrefixIgnoreCase(Book desiredBook) {
		String namePrefix = desiredBook.getName(),
			 authorPrefix = desiredBook.getAuthor();
		if(namePrefix == null && authorPrefix == null) {
			return List.of();
		}
		if(!namePrefix.isEmpty() && !authorPrefix.isEmpty()) {
			List<Book> books =  booksRepository.findByNameStartingWithIgnoreCaseOrAuthorStartingWithIgnoreCase(namePrefix, authorPrefix);
			for(Book book : books) {
				if(book.getAssignedAt() != null) {
					bookOverdueCheckSet(book);
				}
			}
			return books;
		} else if(!namePrefix.isEmpty() && authorPrefix.isEmpty()) {
			List<Book> books = booksRepository.findByNameStartingWithIgnoreCase(namePrefix);
			for(Book book : books) {
				if(book.getAssignedAt() != null) {
					bookOverdueCheckSet(book);
				}
			}
			return books;
		} else if(namePrefix.isEmpty() && !authorPrefix.isEmpty()) {
			List<Book> books = booksRepository.findByAuthorStartingWithIgnoreCase(authorPrefix);
			for(Book book : books) {
				if(book.getAssignedAt() != null) {
					bookOverdueCheckSet(book);
				}
			}
			return books;
		} else {
			return List.of();
		}
	}
	
	public Book findOne(int id) {
		Optional<Book> foundBook = booksRepository.findById(id);
		if(foundBook.get() != null & foundBook.get().getAssignedAt() != null) {
			bookOverdueCheckSet(foundBook.get());
		}
		return foundBook.orElse(null);
	}
	private void bookOverdueCheckSet(Book book) {
		Instant now = Instant.now();
		Date bookCreatedAt = book.getAssignedAt();
		Instant bookCreatedAtInstant = bookCreatedAt.toInstant();
		Duration duration = Duration.between(bookCreatedAtInstant, now);
		long hours = duration.toMinutes();
		if(hours > 30)
			book.setBookReturnTimeIsOverdue(true);
	}
	
	public List<Book> findByOuthor(String author) {
		return booksRepository.findByAuthor(author);
	}
	
	@Transactional
	public void save(Book book) {
		book.setCreatedAt(new Date());
		booksRepository.save(book);
	}
	
	@Transactional
	public void update(int id, Book updatedBook) {
		updatedBook.setId(id);
		booksRepository.save(updatedBook);
	}
	
	@Transactional
	public void assign(Person person, Book book) {
		book.setOwner(person);
		book.setAssignedAt(new Date());
		booksRepository.save(book);
	}
	
	@Transactional
	public void release(Book book) {
		book.setOwner(null);
		book.setAssignedAt(null);
		booksRepository.save(book);
	}
	
	@Transactional
	public void delete(int id) {
		booksRepository.deleteById(id);
	}
}
