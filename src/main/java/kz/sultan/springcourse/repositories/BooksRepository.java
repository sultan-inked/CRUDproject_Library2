package kz.sultan.springcourse.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.sultan.springcourse.models.Book;
import kz.sultan.springcourse.models.Person;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
	
	List<Book> findByAuthor(String author);
	
	List<Book> findAllByOwner(Person person);
	
	List<Book> findAllByName(String name);
	
	List<Book> findByNameStartingWithIgnoreCase(String prefix);
	List<Book> findByAuthorStartingWithIgnoreCase(String prefix);
	List<Book> findByNameStartingWithIgnoreCaseOrAuthorStartingWithIgnoreCase(String namePrefix, String authorPrefix);
}
