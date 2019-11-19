package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Publisher;

public class PubDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>> {

	
	public void savePub(Publisher pub) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?,?,?)", new Object[] { pub.getPubName(), pub.getPubAddress(), pub.getPubPhone() });
	}
	
	public void updatePub(Publisher pub) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_publisher SET publisherName = ? where publisherId = ?",
				new Object[] { pub.getPubName(), pub.getPubId() });

	}

	public void updatePubName(Publisher pub) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_publisher SET publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[] { pub.getPubName(), pub.getPubAddress(), pub.getPubPhone(), pub.getPubId() });

	}
	
	public void updatePubAddress(Publisher pub) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_publisher SET publisherAddress = ? where publisherId = ?",
				new Object[] { pub.getPubAddress(), pub.getPubId() });
	}
	
	public void updatePubPhone(Publisher pub) throws SQLException, ClassNotFoundException {
		mySqlTemplate.update("UPDATE tbl_publiser SET publisherPhone = ? where publisherId = ?",
				new Object[] { pub.getPubPhone(), pub.getPubId() });
	}
		
	public void deletePub(Publisher pub) throws ClassNotFoundException, SQLException {
		mySqlTemplate.update("delete from tbl_publisher where publisherId = ?", new Object[] { pub.getPubId() });
	}

	public List<Publisher> readAllPublishers() throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_Publisher", this);
	}
	
	public List<Publisher> readAllPubsByName(String pubName) throws ClassNotFoundException, SQLException {
		pubName = "%"+pubName+"%";
		return mySqlTemplate.query("SELECT * FROM tbl_publisher where name like ?", new Object[]{pubName}, this);
	}
	
	public List<Publisher> readPubByBook(Book book) throws ClassNotFoundException, SQLException {
		return mySqlTemplate.query("SELECT * FROM tbl_publisher p INNER JOIN tbl_book b ON p.publisherId=b.pubId where b.bookId = ?", new Object[]{book.getBookId()}, this);
	}
	
	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> pubs = new ArrayList<>();
		while (rs.next()) {
			Publisher p = new Publisher();
			p.setPubId(rs.getInt("publisherId"));
			p.setPubName(rs.getString("publisherName"));
			p.setPubAddress(rs.getString("publisherAddress"));
			p.setPubPhone(rs.getString("publisherPhone"));
			pubs.add(p);
		}
		return pubs;
	}

}
