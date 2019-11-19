/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Borrower;

public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>>{
	
	@Autowired
	AuthorDAO adao;
	
	@Autowired
	GenreDAO gdao;
	
	@Autowired
	PubDAO pdao;
	
	public void saveBook(Book book) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_book (title, pubId) values (?, ?)", new Object[] { book.getTitle(), book.getPublisher().getPubId() });
	}
	
	public Integer saveBookWithID(Book book) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String SAVE_BOOK_ID_QUERY = "INSERT INTO tbl_book (title) values (?)";
		mySqlTemplate.update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement(SAVE_BOOK_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, book.getTitle());
	          return ps;
	        }, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public void updateBook(Book book) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_book SET title = ? where bookId = ?",
				new Object[] { book.getTitle(), book.getBookId() });
	}
	
	public void updateBookPublisher(Book book) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_book SET pubId = ? where bookId = ?",
				new Object[] { book.getPublisher().getPubId(), book.getBookId() });
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("delete from tbl_book where bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readAllBooks() throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_book", this);
	}
	
	public List<Book> readAllBooksByName(String title) throws ClassNotFoundException, SQLException {
		title = "%"+title+"%";
		return mySqlTemplate.query("SELECT * FROM tbl_book where title like ?", new Object[]{title}, this);
	}
	
	public List<Book> readBooksByAuthor(Author author) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT b.bookId, title FROM tbl_book b INNER JOIN tbl_book_authors ba ON  b.bookId=ba.bookId INNER JOIN tbl_author a ON ba.authorId=a.authorId WHERE a.authorId=?", new Object[]{author.getAuthorId()}, this);
	}
	
	public List<Book> readBooksByBnB(Borrower b, Integer branchId) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT b.title, b.bookId FROM tbl_book_loans bl INNER JOIN tbl_book b ON bl.bookId=b.bookId WHERE cardNo=? AND branchId=? AND dateIn IS NULL", new Object[] {b.getCardNo(), branchId}, this);
	}
	
	public List<Book> haveBookInBranch(Integer bookId, Integer branchId) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT b.title, b.bookId FROM tbl_book_copies bc INNER JOIN tbl_book b ON bc.bookId=b.bookId WHERE b.bookId = ? AND branchId=?", new Object[] {bookId, branchId}, this);
	}
	
	public List<Book> readBookWithDetails(String title) throws ClassNotFoundException, SQLException {
		title = "%"+title+"%";
		List<Book> ret = new ArrayList<>();
		ret = mySqlTemplate.query("SELECT * FROM tbl_book where title like ?", new Object[]{title}, this);
		for(Book book : ret) {
			book.setAuthors(adao.readAuthorsByBook(book));
			book.setGenres(gdao.readGenresByBook(book));
			book.setPublisher(pdao.readPubByBook(book).get(0));
		}
		return ret;
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book b = new Book();
			b.setBookId(rs.getInt("bookId"));
			b.setTitle(rs.getString("title"));
			books.add(b);
		}
		return books;
	}

}
