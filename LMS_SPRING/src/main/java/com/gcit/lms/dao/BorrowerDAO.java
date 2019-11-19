package com.gcit.lms.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Borrower;

public class BorrowerDAO extends BaseDAO<Borrower> implements ResultSetExtractor<List<Borrower>> {

	public boolean validBorrower(Integer cardNo) throws SQLException {
		List<Borrower> borrs = mySqlTemplate.query("SELECT * FROM tbl_borrower WHERE cardNo=?", new Object[] {cardNo}, this);
		if(borrs.size() == 1)
			return true;
		return false;
	}
	
	public void updateBorrower(Borrower borrower) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_Borrower SET name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNo() });
	}
	
	public void saveBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_Borrower (name, address, phone) values (?,?,?)", new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone() });
	}
	
	public Integer saveBorrowerWithId(Borrower borrower) throws ClassNotFoundException, SQLException {		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String SAVE_BORROWER_ID_QUERY = "INSERT INTO tbl_Borrower (name, address, phone) values (?,?,?)";
		mySqlTemplate.update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement(SAVE_BORROWER_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, borrower.getName());
	          ps.setString(2, borrower.getAddress());
	          ps.setString(3, borrower.getPhone());
	          return ps;
	        }, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public void updateBorrowerName(Borrower borrower) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_Borrower SET name = ? where cardNo = ?",
				new Object[] { borrower.getName(), borrower.getCardNo() });

	}
	
	public void updateBorrowerAddress(Borrower borrower) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_Borrower SET address = ? where cardNo = ?",
				new Object[] { borrower.getAddress(), borrower.getCardNo() });
	}
	
	public void updateBorrowerPhone(Borrower borrower) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_Borrower SET phone = ? where cardNo = ?",
				new Object[] { borrower.getPhone(), borrower.getCardNo() });
	}
		

	public void deleteBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("delete from tbl_Borrower where cardNo = ?", new Object[] { borrower.getCardNo() });
	}

	public List<Borrower> readAllBorrowers() throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_Borrower", this);
	}
	
	public List<Borrower> readAllBorrowersByName(String borrowerName) throws ClassNotFoundException, SQLException {
		borrowerName = "%"+borrowerName+"%";
		return mySqlTemplate.query("SELECT * FROM tbl_Borrower where name like ?", new Object[]{borrowerName}, this);
	}

	public Borrower getLoansAtBranch(Borrower b, Integer branchId) throws SQLException, ClassNotFoundException {
		Borrower ret = b;
		Integer cardNo = b.getCardNo();
		BookDAO bdao = new BookDAO();
		List<Book> books = bdao.readBooksByBnB(b, branchId);
		ret.setBooks(books);
		return ret;
	}
	
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<>();
		while (rs.next()) {
			Borrower a = new Borrower();
			a.setCardNo(rs.getInt("cardNo"));
			a.setName(rs.getString("name"));
			a.setAddress(rs.getString("address"));
			a.setPhone(rs.getString("phone"));
			borrowers.add(a);
		}
		return borrowers;
	}
}
