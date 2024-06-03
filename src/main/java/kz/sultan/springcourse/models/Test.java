package kz.sultan.springcourse.models;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import kz.sultan.springcourse.repositories.BooksRepository;
import kz.sultan.springcourse.services.BooksService;

public class Test {
	
	@Autowired
	static BooksRepository booksRepository;
	public static void main(String... args) {
		BooksService bS = new BooksService(booksRepository);
		
//		Date dateNow = new Date();
//		Book book = bS.findOne(1);
//		Date createdAt = book.getCreatedAt();
//		Date difDate = createdAt - dateNow;
//		System.out.println(difDate);
		
		// Take cuurent date ans time
		Instant now = Instant.now();
		
		// Book creation time
		Date bookCreatedAt = bS.findOne(1).getCreatedAt();
		
		// Convert the creation date to an Instant object
		Instant bookCreatedAtInstant = bookCreatedAt.toInstant();
		
		// Calculate the time between the present moment and the moment of creation
		Duration duration = Duration.between(bookCreatedAtInstant, now);
		
		// Output the difference in milliseconds
		long ms = duration.toHours();
		System.out.println(ms);
		
	}
}
