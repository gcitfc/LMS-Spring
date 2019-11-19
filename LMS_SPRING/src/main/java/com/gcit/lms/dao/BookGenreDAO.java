package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookGenre;
import com.gcit.lms.entity.Genre;

public class BookGenreDAO extends BaseDAO<BookGenre> implements ResultSetExtractor<List<BookGenre>> {
	
	public void saveBookGenre(Genre genre, Book book) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_book_genres (genre_id, bookId) values (?, ?)", new Object[] { genre.getGenreId(), book.getBookId() });
	}
	
	public void deleteBookGenre(Book book) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("delete from tbl_book_genres where bookId = ?", new Object[] { book.getBookId() });
	}

	@Override
	public List<BookGenre> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
