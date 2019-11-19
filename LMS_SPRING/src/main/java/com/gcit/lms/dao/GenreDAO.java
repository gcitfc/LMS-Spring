package com.gcit.lms.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;

public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>>{
	
	public void saveGenre(Genre genre) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_genre (genre_name) values (?)", new Object[] { genre.getGenreName() });
	}

	public void updateGenre(Genre genre) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_genre SET genre_name = ? where genre_id = ?",
				new Object[] { genre.getGenreName(), genre.getGenreId() });
	}

	public void deleteGenre(Genre genre) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("delete from tbl_genre where genre_id = ?", new Object[] { genre.getGenreId() });
	}

	public List<Genre> readAllGenre() throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_genre", this);
	}
	
	public List<Genre> readAllGenreByName(String genreName) throws ClassNotFoundException, SQLException {
		genreName = "%"+genreName+"%";
		return mySqlTemplate.query("SELECT * FROM tbl_genre where authorName like ?", new Object[]{genreName}, this);
	}
	
	public List<Genre> readGenresByBook(Book book) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT g.genre_id, g.genre_name FROM tbl_book b INNER JOIN tbl_book_genres bg ON b.bookId=bg.bookId INNER JOIN tbl_genre g ON bg.genre_id=g.genre_id WHERE b.bookId=?", new Object[]{book.getBookId()}, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> genres = new ArrayList<>();
		while (rs.next()) {
			Genre g = new Genre();
			g.setGenreId(rs.getInt("genre_id"));
			g.setGenreName(rs.getString("genre_name"));
			genres.add(g);
		}
		return genres;
	}

}
