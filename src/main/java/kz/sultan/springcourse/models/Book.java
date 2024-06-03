package kz.sultan.springcourse.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Book")
public class Book {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "Name of book should not be empty")
	@Size(min = 1, max = 100, message = "Name of book should be between 1 to 100 characters")
	@Column(name = "name")
	private String name;
	
	@NotNull(message = "Author of book should not be empty")
	@Size(min = 2, max = 60, message = "Author of book should be between 2 to 60 characters")
	@Column(name = "author")
	private String author;
	
	@Column(name = "year")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date year;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "assigned_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date assignedAt;
	
	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "id")
	private Person owner;
	
	@Transient
	private boolean bookReturnTimeIsOverdue;
	
	
	
	public Book() {}

	public Book(String name, String author, Date year, Date createdAt, Date assignedAt) {
		this.name = name;
		this.author = author;
		this.year = year;
		this.createdAt = createdAt;
		this.assignedAt = assignedAt;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", author=" + author + ", year=" + year + ", createdAt="
				+ createdAt + ", owner=" + owner + "]";
	}

	public Date getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(Date assignedAt) {
		this.assignedAt = assignedAt;
	}

	public boolean getBookReturnTimeIsOverdue() {
		return bookReturnTimeIsOverdue;
	}

	public void setBookReturnTimeIsOverdue(boolean bookReturnTimeIsOverdue) {
		this.bookReturnTimeIsOverdue = bookReturnTimeIsOverdue;
	}

	
}
