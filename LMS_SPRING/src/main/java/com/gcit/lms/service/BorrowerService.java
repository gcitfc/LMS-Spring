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

import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class BorrowerService {
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
	
	@RequestMapping(value="/borrower/validCardNo", method=RequestMethod.GET, produces="application/json")
	public boolean validCardNo(@RequestParam Integer cardNo) throws SQLException{
		try {
			return borrdao.validBorrower(cardNo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Transactional
	@RequestMapping(value="/borrower/checkOutBook", method=RequestMethod.POST, consumes="application/json")
	public String checkOutBook(@RequestBody BookLoans bl) throws SQLException{
		try {
			List<BookCopies> copies = bcdao.readCopiesByBranch(bl.getBranch());
			for(BookCopies itr : copies) {
				if(itr.getBook().getBookId() == bl.getBook().getBookId()) {
					bcdao.decBookCopiesBy1(itr);
					break;
				}
			}
			bldao.saveLoan(bl);
			return "Book cheched out sucessfully";
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to check out book, try again";
		}
	}
	
	@Transactional
	@RequestMapping(value="/borrower/returnBook", method=RequestMethod.POST, consumes="application/json")
	public String returnBook(@RequestBody BookLoans bl) throws SQLException{
		try {
			List<BookCopies> copies = bcdao.readCopiesByBranch(bl.getBranch());
			for(BookCopies itr : copies) {
				if(itr.getBook().getBookId() == bl.getBook().getBookId()) {
					bcdao.incBookCopiesBy1(itr);
					break;
				}
			}
			bldao.updateReturnDate(bl);
			return "Book returned sucessfully";
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "Unable to return book, try again";
		}
	}
	
}
