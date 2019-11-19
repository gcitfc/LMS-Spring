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

import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Book;

public class BranchDAO extends BaseDAO<Branch> implements ResultSetExtractor<List<Branch>>{

	
	public void saveBranch(Branch branch) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_library_branch (branchName, branchAddress) values (?, ?)", new Object[] { branch.getBranchName(), branch.getAddress() });
	}
	
	public Integer saveBranchWithID(Branch branch) throws ClassNotFoundException, SQLException {		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String SAVE_BRANCH_ID_QUERY = "INSERT INTO tbl_library_branch (branchName, branchAddress) values (?, ?)";
		mySqlTemplate.update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement(SAVE_BRANCH_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, branch.getBranchName());
	          ps.setString(2, branch.getAddress());
	          return ps;
	        }, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public void updateBranch(Branch branch) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_library_branch SET branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] { branch.getBranchName(), branch.getAddress(), branch.getBranchId() });

	}

	public void updateBranchName(Branch branch) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_library_branch SET branchName = ? where branchId = ?",
				new Object[] { branch.getBranchName(), branch.getBranchId() });

	}
	
	public void updateBranchAddress(Branch branch) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_library_branch SET branchAddress = ? where branchId = ?",
				new Object[] { branch.getAddress(), branch.getBranchId() });

	}

	public void deleteBranch(Branch branch) throws ClassNotFoundException, SQLException {
		mySqlTemplate.query("delete from tbl_library_branch where branchId = ?", new Object[] { branch.getBranchId() }, this);
	}

	public List<Branch> readAllBranches() throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_library_branch", this);
	}
	
	public List<Branch> readBranchByName(String branchName) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_library_branch where branchName=?", new Object[]{branchName}, this);
	}

	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		while (rs.next()) {
			Branch b = new Branch();
			b.setBranchId(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setAddress(rs.getString("branchAddress"));
			branches.add(b);
		}
		return branches;
	}
	
	
	public void updateBookCopies(Integer branchId, Integer bookId, Integer copies) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("UPDATE tbl_book_copies SET noOfCopies = ? where branchId = ? AND bookId = ?",
				new Object[] { copies, branchId, bookId });
	}
	
	
	public void addNewBook(Branch branch, Book book, Integer copies) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)", new Object[] { book.getBookId(), branch.getBranchId(), copies });
	}
	
}
