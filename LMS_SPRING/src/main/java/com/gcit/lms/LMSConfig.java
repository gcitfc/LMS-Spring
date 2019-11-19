/**
 * 
 */
package com.gcit.lms;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookAuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookGenreDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PubDAO;


@Configuration
public class LMSConfig {
	public final String url = "jdbc:mysql://localhost:3306/library?useSSL=false";
	public final String userName = "root";
	public final String password = "00000000";
	public final String driver = "com.mysql.cj.jdbc.Driver";
	
	@Bean
	public BasicDataSource dataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setPassword(password);
		ds.setUsername(userName);
		return ds;
	}
	
	@Bean
	public BasicDataSource dataSourceOracle(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setPassword(password);
		ds.setUsername(userName);
		return ds;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(){
		return new JdbcTemplate(dataSource());
	}
	
//	@Bean
//	@Qualifier(value="oracleTemplate")
//	public JdbcTemplate jdbcTemplateOracle(){
//		return new JdbcTemplate(dataSourceOracle());
//	}
	
	@Bean
	public AuthorDAO adao(){
		return new AuthorDAO();
	}
	
	@Bean
	public BookDAO bdao(){
		return new BookDAO();
	}
	
	@Bean
	public GenreDAO gdao(){
		return new GenreDAO();
	}
	
	@Bean
	public PubDAO pdao(){
		return new PubDAO();
	}
	
	@Bean
	public BranchDAO brdao(){
		return new BranchDAO();
	}
	
	@Bean
	public BorrowerDAO borrdao() {
		return new BorrowerDAO();
	}
	
	@Bean
	public BookLoansDAO bldao(){
		return new BookLoansDAO();
	}
	
	@Bean
	public BookCopiesDAO bcdao(){
		return new BookCopiesDAO();
	}
	
	@Bean
	public BookAuthorDAO badao() {
		return new BookAuthorDAO();
	}

	@Bean
	public BookGenreDAO bgdao() {
		return new BookGenreDAO();
	}
	
	@Bean
	public PlatformTransactionManager txManager(){
		return new DataSourceTransactionManager(dataSource());
	}
}