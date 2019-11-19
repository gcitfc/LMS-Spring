package com.gcit.lms.service;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookAuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookGenreDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PubDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

@RestController
public class AdministratorService {
	
	@Autowired
	AuthorDAO adao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	GenreDAO gdao;
	
	@Autowired
	PubDAO pdao;
	
	@Autowired
	BranchDAO brdao;
	
	@Autowired
	BorrowerDAO borrdao;
	
	@Autowired
	BookLoansDAO bldao;
	
	@Autowired
	BookAuthorDAO badao;
	
	@Autowired
	BookGenreDAO bgdao;
	
	@Transactional
	@RequestMapping(value="/admin/updateAuthor", method=RequestMethod.POST, consumes="application/json")
	public String updateAuthor(@RequestBody Author author) throws SQLException{
		try {
			if(author!=null){
				if(author.getAuthorId()!=null && author.getAuthorName()!=null){
					adao.updateAuthor(author);
					return "Author updated sucessfully";
				}else if(author.getAuthorId()!=null){
					adao.deleteAuthor(author);
					return "Author deleted sucessfully";
				}else{
					adao.saveAuthor(author);
					return "Author saved sucessfully";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to save Author, try again";
		}
		return null;
	}
	
	@Transactional
	public void saveBook(Book book) throws SQLException, ClassNotFoundException{
			Integer bookId = bdao.saveBookWithID(book);
			book.setBookId(bookId);
			for(Author a : book.getAuthors()) {
				badao.saveBookAuthor(a, book);
			}
			for(Genre g : book.getGenres()) {
				bgdao.saveBookGenre(g, book);
			}
	}
	
	@RequestMapping(value="/admin/readAllBooksDetail", method=RequestMethod.GET, produces="application/json")
	public List<Book> readBookDetail(@RequestParam String searchString) throws SQLException{
		List<Book> books = new ArrayList<>();
		try {
			if(searchString != null)
				books= bdao.readBookWithDetails(searchString);
			else
				books= bdao.readBookWithDetails("");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	@RequestMapping(value="/admin/readAuthors/{searchString}", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAuthors(@PathVariable String searchString) throws SQLException{
		List<Author> authors = new ArrayList<>();
		try {
			if(searchString!=null){
				authors= adao.readAllAuthorsByName(searchString);
			}else{
				authors = adao.readAllAuthors();
			}
			for(Author a: authors){
				a.setBooks(bdao.readBooksByAuthor(a));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return authors;
	}
	
	@RequestMapping(value="/admin/readAuthorsByName", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAuthorsByName(@RequestParam String searchString) throws SQLException{
		List<Author> authors = new ArrayList<>();
		try {
			if(searchString!=null){
				authors= adao.readAllAuthorsByName(searchString);
			}else{
				authors = adao.readAllAuthors();
			}
			for(Author a: authors){
				a.setBooks(bdao.readBooksByAuthor(a));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return authors;	
	}
	
	
	@RequestMapping(value="/admin/getLoansByBnB", method=RequestMethod.GET, produces="application/json")
	public List<BookLoans> getLoansByBnB(@RequestParam Integer cardNo, Integer branchId) throws SQLException{
		Borrower b = new Borrower();
		b.setCardNo(cardNo);
		Branch br = new Branch();
		br.setBranchId(branchId);
		try {
			return bldao.readLoansByBB(b, br);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	@Transactional
	@RequestMapping(value="/admin/updateBook", method=RequestMethod.POST, consumes="application/json")
	public String updateBook(@RequestBody Book book) throws SQLException{
		try {
			if(book!=null){
				if(book.getBookId()!=null && (book.getTitle()!=null || book.getPublisher()!=null)){
					bdao.updateBook(book);
					return "Book updated sucessfully";
				}else if(book.getBookId()!=null){
					bdao.deleteBook(book);
					return "Book deleted sucessfully";
				}else{
					bdao.saveBook(book);
					return "Book saved sucessfully";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to save Book, try again";
		}
		return null;
	}
	
	@RequestMapping(value="/admin/readBooks", method=RequestMethod.GET, produces="application/json")
	public List<Book> readBooks(@RequestParam String searchString) throws SQLException{
		List<Book> books = new ArrayList<>();
		try {
			if(searchString!=null){
				books= bdao.readAllBooksByName(searchString);
			}else{
				books = bdao.readAllBooks();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return books;
	}
	
	@Transactional
	@RequestMapping(value="/admin/updateGenre", method=RequestMethod.POST, consumes="application/json")
	public String updateGenre(@RequestBody Genre genre) throws SQLException{
		try {
			if(genre!=null){
				if(genre.getGenreId()!=null && genre.getGenreName()!=null){
					gdao.updateGenre(genre);
					return "Genre updated sucessfully";
				}else if(genre.getGenreId()!=null){
					gdao.deleteGenre(genre);
					return "Genre deleted sucessfully";
				}else{
					gdao.saveGenre(genre);
					return "Genre saved sucessfully";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to save Genre, try again";
		}
		return null;
	}
	
	@RequestMapping(value="/admin/readGenres", method=RequestMethod.GET, produces="application/json")
	public List<Genre> readGenres(@RequestParam String searchString) throws SQLException{
		List<Genre> genres = new ArrayList<>();
		try {
			if(searchString!=null){
				genres= gdao.readAllGenreByName(searchString);
			}else{
				genres = gdao.readAllGenre();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return genres;
	}
	
	@Transactional
	@RequestMapping(value="/admin/updatePublisher", method=RequestMethod.POST, consumes="application/json")
	public String updatePublisher(@RequestBody Publisher publisher) throws SQLException{
		try {
			if(publisher!=null){
				if(publisher.getPubId()==null && publisher.getPubName()!=null && publisher.getPubAddress() != null && publisher.getPubPhone() != null){
					pdao.savePub(publisher);
					return "Publisher saved sucessfully";
				}else if(publisher.getPubId()!=null && publisher.getPubName()==null && publisher.getPubAddress() == null && publisher.getPubPhone() == null){
					pdao.deletePub(publisher);
					return "Publisher deleted sucessfully";
				}else {
					pdao.updatePub(publisher);
					return "Publisher updated sucessfully";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to save Publisher, try again";
		}
		return null;
	}
	
	
	@RequestMapping(value="/admin/readPublishers", method=RequestMethod.GET, produces="application/json")
	public List<Publisher> readPublishers(@RequestParam String searchString) throws SQLException{
		List<Publisher> pubs = new ArrayList<>();
		try {
			if(searchString!=null){
				pubs= pdao.readAllPubsByName(searchString);
			}else{
				pubs = pdao.readAllPublishers();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return pubs;
	}
	
	@Transactional
	@RequestMapping(value="/admin/updateBranch", method=RequestMethod.POST, consumes="application/json")
	public String updateBranch(@RequestBody Branch branch) throws SQLException{
		try {
			if(branch!=null){
				if(branch.getBranchId()==null && branch.getBranchName()!=null && branch.getAddress() != null){
					brdao.saveBranch(branch);
					return "Branch saved sucessfully";
				}else if(branch.getBranchId()!=null && branch.getBranchName()==null && branch.getAddress() == null ){
					brdao.deleteBranch(branch);
					return "Branch deleted sucessfully";
				}else {
					brdao.updateBranch(branch);
					return "Branch updated sucessfully";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to save Branch, try again";
		}
		return null;
	}
	
	@RequestMapping(value="/admin/readBranches", method=RequestMethod.GET, produces="application/json")
	public List<Branch> readBranches(@RequestParam String searchString) throws SQLException{
		List<Branch> branches = new ArrayList<>();
		try {
			if(searchString!=null){
				branches= brdao.readBranchByName(searchString);
			}else{
				branches = brdao.readAllBranches();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return branches;
	}
	
	@Transactional
	@RequestMapping(value="/admin/updateBorrower", method=RequestMethod.POST, consumes="application/json")
	public String updateBorrower(@RequestBody Borrower borrower) throws SQLException{
		try {
			if(borrower!=null){
				if(borrower.getCardNo()==null && borrower.getName()!=null && borrower.getAddress() != null && borrower.getPhone()!=null){
					int cardNo = borrdao.saveBorrowerWithId(borrower);
					return "Borrower saved sucessfully, Card#" + cardNo;
				}else if(borrower.getCardNo()!=null && borrower.getName()==null && borrower.getAddress() == null && borrower.getPhone()==null){
					borrdao.deleteBorrower(borrower);
					return "Borrower deleted sucessfully";
				}else {
					borrdao.updateBorrower(borrower);
					return "Borrower updated sucessfully";
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to save Borrower, try again";
		}
		return null;
	}
	
	@RequestMapping(value="/admin/readBorrowers", method=RequestMethod.GET, produces="application/json")
	public List<Borrower> readBorrowers(@RequestParam String searchString) throws SQLException{
		List<Borrower> borrowers = new ArrayList<>();
		try {
			if(searchString!=null){
				borrowers= borrdao.readAllBorrowersByName(searchString);
			}else{
				borrowers = borrdao.readAllBorrowers();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return borrowers;
	}
	
	@Transactional
	@RequestMapping(value="/admin/overrideDue", method=RequestMethod.POST, consumes="application/json")
	public String overrideDue(@RequestBody BookLoans loans) throws SQLException{
		try {
			bldao.updateLoanDue(loans);
			return "Due date overriden sucessfully";
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to override Due Date, try again";
		}
	}

}