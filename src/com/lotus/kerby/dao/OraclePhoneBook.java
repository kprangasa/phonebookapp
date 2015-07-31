package com.lotus.kerby.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lotus.kerby.domain.Company;
import com.lotus.kerby.domain.Contact;

public class OraclePhoneBook implements PhoneBook{
	private static OraclePhoneBook instance = null;
	
	public static OraclePhoneBook getInstance(){
		if(instance == null){
			instance = new OraclePhoneBook();
		}
		
		return instance;
	}
	private OraclePhoneBook(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Unable to establish database connection.");
		}
	}
	
	private static Connection getConnection() throws SQLException{
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SAMPLE_KERBY",
				"password");
			connection.setAutoCommit(false);
		
		return connection;
	}
	public List<Contact> list() {
		Connection connection = null;
		Statement statement = null;	
		List<Contact> contacts = new ArrayList<Contact>();
		
		try {
			connection = getConnection();
			statement = connection.createStatement();
			String sql = "SELECT id, name, birthday, vipYN, companyid, CONTACT_NUMBER FROM CONTACTS";
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				contacts.add(extractContactFromResult(resultSet));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Database error.");
		}finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close connection.");
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close statement");
				}
			}
		}
		
		
		return contacts;
	}

//	public Contact show(Contact contactToSearch) {
//		Connection connection = null;
//		PreparedStatement statement = null;	
//		Contact contact = null;
//		try {
//			connection = getConnection();
//			statement = connection.prepareStatement("SELECT id, name, birthday, vipYN, companyid, CONTACT_NUMBER FROM CONTACTS WHERE name = ?");
//			statement.setString(1, contactToSearch.getName());
//			ResultSet resultSet = statement.executeQuery();
//			if(resultSet.next()){
//				contact = extractContactFromResult(resultSet);
//			}
//		} catch (SQLException e) {
//			throw new RuntimeException("Database error.");
//		}finally{
//			if(connection!=null){
//				try {
//					connection.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					throw new RuntimeException("Unable to close connection.");
//				}
//			}
//			if(statement!=null){
//				try {
//					statement.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					throw new RuntimeException("Unable to close statement");
//				}
//			}
//		}
//		
//		
//		return contact;
//	}

	public List<Contact> search(String keyword) {
		Connection connection = null;
		PreparedStatement statement = null;	
		List<Contact> contacts = new ArrayList<Contact>();
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement("SELECT id, name, birthday, vipYN, companyid, CONTACT_NUMBER FROM CONTACTS WHERE lower(name) like ? or CONTACT_NUMBER like ?");
			statement.setString(1, keyword.toLowerCase()+"%");
			statement.setString(2, keyword+"%");
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				contacts.add(extractContactFromResult(resultSet));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Database error.");
		}finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close connection.");
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close statement");
				}
			}
		}
		
		
		return contacts;
	}

	public void delete(Contact contact) {
		if(contact == null){
			throw new IllegalArgumentException("Deleting non existing contact.");
		}
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement("DELETE FROM CONTACTS WHERE id = ?");
			statement.setLong(1, contact.getId());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Database error;");
				}
			throw new RuntimeException("Database error;");
		}finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close connection.");
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close statement");
				}
			}
		}
		
	}

	public void update(Contact contact) {
		if(contact == null){
			throw new IllegalArgumentException("Updating non existing contact.");
		}
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement("UPDATE CONTACTS set name = ? , birthday = ?, vipYN = ?, companyID = ?, CONTACT_NUMBER = ? WHERE id = ?");
			statement.setString(1, contact.getName());
			Date birthday = new Date(contact.getBirthday().getTime());
			statement.setDate(2, birthday);
			statement.setBoolean(3, contact.isVIP());
			statement.setLong(4, contact.getCompanyID());
			statement.setString(5, contact.getContactNumber());
			statement.setLong(6, contact.getId());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Database error;");
			}
		throw new RuntimeException("Database error;");
		}finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close connection.");
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close statement");
				}
			}
		}
		
	}

	public void create(Contact contact) {
		Connection connection = null;
		PreparedStatement statement = null;
		Date birthday;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement("INSERT INTO CONTACTS (id, name, birthday, vipYN, companyid, CONTACT_NUMBER) VALUES(contacts_seq.nextval,?,?,?,?,?)");
			statement.setString(1, contact.getName());
			if(contact.getBirthday() == null){
				birthday = null;
			}else{
			birthday =  new Date(contact.getBirthday().getTime());
			}
			statement.setDate(2, birthday);
			statement.setBoolean(3, contact.isVIP());
			
			statement.setObject(4, contact.getCompanyID());
			
			statement.setString(5, contact.getContactNumber());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				
				throw new RuntimeException("Database error.");
			}
			throw new RuntimeException("Database error.");
		}
		finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					
					throw new RuntimeException("Unable to close connection");
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close statement");
				}
			}
		}
		
	}
	public Contact getByName(String name){
		if(name == null || name.isEmpty()){
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		Contact contact = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try{
			connection = getConnection();
			statement = connection.prepareStatement("SELECT id , name, birthday, vipYN, companyID, CONTACT_NUMBER FROM CONTACTS WHERE name = ? ");
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
		
			if(rs.next()){
				contact = extractContactFromResult(rs);
			}
			
		}catch(SQLException e){
			
			throw new IllegalStateException("Database error.");
		}
		finally {

			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return  contact;
	}
	private static Contact extractContactFromResult(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		String name = rs.getString("name");
		Date birthday = rs.getDate("birthday");
		boolean isVIP = rs.getBoolean("vipYN");
		long companyID = rs.getLong("companyid");
		String contactNumber = rs.getString("CONTACT_NUMBER");
		Contact contact = new Contact(id, name, birthday, isVIP, companyID, contactNumber);
		return contact;
	}
	private static Company extractCompanyFromResult(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		String name = rs.getString("name");
		String code = rs.getString("code");
		String description= rs.getString("description");
		Company company = new Company(id, name, code, description);
		return company;
	}
	public Company getCompany(Contact contact) throws SQLException {
		if(contact == null){
			throw new IllegalArgumentException("Providing non existing contact.");
		}
		
		Company company = null;
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement("SELECT id, name, code, description FROM COMPANIES WHERE COMPANIES.id = ?");
			statement.setLong(1, contact.getCompanyID());
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()){
				company = extractCompanyFromResult(rs);
			}
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Database error;");
			}
		throw new RuntimeException("Database error;");
		}finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close connection.");
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close statement");
				}
			}
		}
		return company;
		
	}
	public Company getCompanyByCode(String companyCode) throws SQLException {
		Company company = null;
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement("SELECT id, name, code, description FROM COMPANIES WHERE COMPANIES.code = ?");
			statement.setString(1, companyCode);
			ResultSet rs = statement.executeQuery();
			if(rs.next()){
				company = extractCompanyFromResult(rs);
			}
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Database error;");
			}
		throw new RuntimeException("Database error;");
		}finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close connection.");
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Unable to close statement");
				}
			}
		}
		return company;
		
	}

}
