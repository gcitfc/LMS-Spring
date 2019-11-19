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
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PubDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;


@RestController
public class LibrarianService {
	
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
	BookCopiesDAO bcdao;

	@Transactional
	@RequestMapping(value="/lib/updateBranch", method=RequestMethod.POST, consumes="application/json")
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
	
	@Transactional
	@RequestMapping(value="/lib/updateCopies", method=RequestMethod.POST, consumes="application/json")
	public String updateCopies(@RequestBody BookCopies bookcopy) throws SQLException{
		try {
			if(bdao.haveBookInBranch(bookcopy.getBook().getBookId() ,bookcopy.getBranch().getBranchId()).size() == 0) {
				bcdao.saveBookCopies(bookcopy);
				return "Num of Copies saved sucessfully";
			}
			else if (bookcopy.getNoOfCopies() != 0) {
				bcdao.updateBookCopies(bookcopy);
				return "Num of Copies updated sucessfully";
			}
			else {
				bcdao.deleteBookCopies(bookcopy);
				return "Num of Copies deleted sucessfully";
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to update Num of Copies, try again";
		}
	}

}
