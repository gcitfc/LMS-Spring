package com.gcit.lms.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookAuthor;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Branch;

public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>> {
	
	public void saveBookCopies(BookCopies bc) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", new Object[] { bc.getBook().getBookId(), bc.getBranch().getBranchId(), bc.getNoOfCopies() });
	}
	
	public void updateBookCopies(BookCopies bc) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("UPDATE tbl_book_copies SET noOfCopies = ? WHERE (bookId = ?) and (branchId = ?)", new Object[] { bc.getNoOfCopies(), bc.getBook().getBookId(), bc.getBranch().getBranchId()});
	}
	
	public void deleteBookCopies(BookCopies bc) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("DELETE from tbl_book_copies where (bookId = ?) AND (branchId = ?)", new Object[] { bc.getBook().getBookId(), bc.getBranch().getBranchId() });
	}
	
	public void decBookCopiesBy1(BookCopies bc) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("UPDATE tbl_book_copies SET noOfCopies = noOfCopies-1 WHERE (bookId = ?) and (branchId = ?)", new Object[] { bc.getBook().getBookId(), bc.getBranch().getBranchId()});
	}
	
	public void incBookCopiesBy1(BookCopies bc) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("UPDATE tbl_book_copies SET noOfCopies = noOfCopies+1 WHERE (bookId = ?) and (branchId = ?)", new Object[] { bc.getBook().getBookId(), bc.getBranch().getBranchId()});
	}
	
	public List<BookCopies> readCopiesByBranch(Branch branch) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_book_copies bc\n" + 
				"INNER JOIN tbl_library_branch lb ON lb.branchId=bc.branchId\n" + 
				"INNER JOIN tbl_book b ON b.bookId=bc.bookId\n" + 
				"WHERE lb.branchId=?", new Object[] { branch.getBranchId()}, this );
	}

	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<BookCopies> copies = new ArrayList<>();
		while (rs.next()) {
			BookCopies bc = new BookCopies();
			Book book = new Book();
			Branch branch = new Branch();
			branch.setBranchId(rs.getInt("BranchId"));
			branch.setBranchName(rs.getString("BranchName"));
			book.setTitle(rs.getString("title"));
			book.setBookId(rs.getInt("bookId"));
			bc.setBook(book);
			bc.setBranch(branch);
			bc.setNoOfCopies(rs.getInt("noOfCopies"));
			copies.add(bc);
		}
		return copies;
	}

}
