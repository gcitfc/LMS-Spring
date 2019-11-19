package com.gcit.lms.dao;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;

public class BookLoansDAO extends BaseDAO<BookLoans> implements ResultSetExtractor<List<BookLoans>> {
	
	public void saveLoan(BookLoans loan) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) values (?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))", new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo() });
	}
	
	public void updateLoanDue(BookLoans loan) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("UPDATE tbl_book_loans SET dueDate = ? WHERE (bookId = ?) and (branchId = ?) and (cardNo = ?)", new Object[] { loan.getDueDate(), loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo()});
	}
	
	public void updateReturnDate(BookLoans loan) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("UPDATE tbl_book_loans SET dateIn = CURDATE() WHERE (bookId = ?) and (branchId = ?) and (cardNo = ?)", new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchId(), loan.getBorrower().getCardNo()});
	}
	
	public List<BookLoans> readLoansByBB(Borrower borrower, Branch branch) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_book_loans bl INNER JOIN tbl_borrower br ON bl.cardNo=br.cardNo INNER JOIN tbl_library_branch lb ON lb.branchId=bl.branchId INNER JOIN tbl_book b ON b.bookId=bl.bookId WHERE bl.cardNo=? AND bl.branchId=?", new Object[] { borrower.getCardNo(), branch.getBranchId()}, this );
	}
	
	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException {
		List<BookLoans> loans = new ArrayList<>();
		while (rs.next()) {
			BookLoans b = new BookLoans();
			Book book = new Book();
			Branch branch = new Branch();
			Borrower borrower = new Borrower();
			book.setTitle(rs.getString("title"));
			book.setBookId(rs.getInt("bookId"));
			b.setBook(book);
			branch.setBranchId(rs.getInt("BranchId"));
			branch.setBranchName(rs.getString("BranchName"));
			b.setBranch(branch);
			borrower.setCardNo(rs.getInt("cardNo"));
			borrower.setName(rs.getString("name"));
			b.setBorrower(borrower);
			
			b.setDateOut(rs.getString("dateOut").substring(0, 10));
			b.setDueDate(rs.getString("dueDate").substring(0, 10));
			
			loans.add(b);
		}
		return loans;
	}
	
}
