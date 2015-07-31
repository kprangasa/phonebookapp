package com.lotus.kerby.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.lotus.kerby.dao.OraclePhoneBook;
import com.lotus.kerby.domain.Company;
import com.lotus.kerby.domain.Contact;

public class Main {

	


	public static void main(String[] args) throws SQLException {

		OraclePhoneBook oraclePhoneBook = OraclePhoneBook.getInstance();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			try {
				showInterface();
				String entry = bufferedReader.readLine();
				switch(entry){
				case "1":
					listAllContacts(oraclePhoneBook);
					break;
				case "2":
					showContact(oraclePhoneBook, bufferedReader);
					break;
					
				case "3":
					searchContacts(oraclePhoneBook, bufferedReader);
					break;
				case "4":
					deleteContact(oraclePhoneBook, bufferedReader);
					break;
				case "5":
					
					createContact(oraclePhoneBook, bufferedReader);
			
					break;
				case "6":
					updateContact(oraclePhoneBook, bufferedReader);
					break;
				case "7":
					return;
				default:
					System.out.print("Invalid option.");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}




	private static void deleteContact(OraclePhoneBook oraclePhoneBook,
		BufferedReader bufferedReader) throws IOException {
		System.out.println("Enter name of contact to delete: ");
		String nameToDelete = bufferedReader.readLine();
		
		if(nameToDelete.isEmpty()){
			System.out.println("Name cannot be empty.");
			return;
		}
		
		Contact contactToDelete = oraclePhoneBook.getByName(nameToDelete);
		if(contactToDelete== null){
			System.out.println("Contact not found.");
			return;
		}
		
		System.out.println("Contact successfully deleted.");
		oraclePhoneBook.delete(contactToDelete);
	}




	private static void showContact(OraclePhoneBook oraclePhoneBook,
		BufferedReader bufferedReader) throws IOException, SQLException {
		System.out.println("Enter name of contact to show: ");
		String nameToShow = bufferedReader.readLine();
		
		if(nameToShow.isEmpty()){
			System.out.println("Name cannot be empty.");
			return;
		}
		
		Contact contactToShow = oraclePhoneBook.getByName(nameToShow);
		if(contactToShow == null){
			System.out.println("Contact not found.");
			return;
		}
		System.out.println(contactToShow + ", " + oraclePhoneBook.getCompany(contactToShow));
	}




	private static void updateContact(OraclePhoneBook oraclePhoneBook,
		BufferedReader bufferedReader) throws IOException {
		System.out.println("Enter name of contact to update: ");
		String nameToUpdate = bufferedReader.readLine();
		System.out.println("Enter contact new contact number: ");
		String contactNumber = bufferedReader.readLine();
		
		if(nameToUpdate.isEmpty()){
			System.out.println("Name cannot be empty.");
			return;
		}
		
		else if(oraclePhoneBook.getByName(nameToUpdate) == null){
			System.out.println("Contact not found.");
			return;
		}
		
		else if(contactNumber.length()==11 && !nameToUpdate.isEmpty() && contactNumber.matches("[0-9]+")){
		Contact contactToUpdate = oraclePhoneBook.getByName(nameToUpdate);
		contactToUpdate.setContactNumber(contactNumber);
		oraclePhoneBook.update(contactToUpdate);
		System.out.println("Update success.");
		}
		
		else{
			System.out.println("Contact number must be 11 digits and must contain numbers only.");
			return;
		}
	}
	/**
	 * 
	 * @param oraclePhoneBook
	 * @param bufferedReader
	 * @throws IOException
	 * @throws SQLException
	 */
	private static void createContact(OraclePhoneBook oraclePhoneBook,
		BufferedReader bufferedReader) throws IOException, SQLException {
		System.out.println("Enter name of contact to add: ");
		String nameToAdd= bufferedReader.readLine();
		System.out.println("Enter birthday of contact to add:(MM/dd/yyyy) ");
		String birthday= bufferedReader.readLine();
		System.out.println("Enter if contact is vip: ");
		String isVip= bufferedReader.readLine();
		System.out.println("Enter contact's company ID: ");
		String companyCode= bufferedReader.readLine();
		System.out.println("Enter name of contact's contact number to add: ");
		String contactNumber= bufferedReader.readLine();
		Date birthdate = null;
		boolean isVIP = false;
		Long companyID = null;
		
		if(nameToAdd.isEmpty()||contactNumber.isEmpty()){
			System.out.println("Name and contact number must not be empty.");
			return;
		}
		if(!birthday.isEmpty()){
		try{	
			birthdate = new Date(birthday);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			throw new IllegalArgumentException("Invalid Date Format");
		}
		}
		if(!isVip.isEmpty()){
			if(isVip.equals("true")||isVip.equals("false")){
				isVIP = Boolean.valueOf(isVip);
			}
			else{
				System.out.println("True or false for isVip.");
				return;
			}
			
		}
		if(!companyCode.isEmpty()){
			Company company = oraclePhoneBook.getCompanyByCode(companyCode);
			if(company == null){
				System.out.println("No company code:" + companyCode);
				return;
			}
			companyID = company.getId();
		}
		Contact newContact = new Contact(nameToAdd, birthdate, isVIP, companyID, contactNumber);
		oraclePhoneBook.create(newContact);
		System.out.println("Contact successfully created.");
		
	}

	/**
	 * List all contacts in database if database is not empty.
	 * @param oraclePhoneBook
	 * @throws SQLException
	 */
	private static void listAllContacts(OraclePhoneBook oraclePhoneBook)
			throws SQLException {
		if(!oraclePhoneBook.list().isEmpty()){
		 for(Contact contact:oraclePhoneBook.list()){
			 System.out.println(contact + ", " + oraclePhoneBook.getCompany(contact));
		 }
		}
		if(oraclePhoneBook.list().isEmpty()){
			System.out.print("No contacts.");
			return;
		}
	}
	/**
	 * 
	 * @param oraclePhoneBook
	 * @param bufferedReader
	 * @throws IOException
	 * @throws SQLException 
	 */
	private static void searchContacts(OraclePhoneBook oraclePhoneBook,
			BufferedReader bufferedReader) throws IOException, SQLException {
		System.out.println("Enter keyword of contact to search: ");
		String keyWord = bufferedReader.readLine();
		for(Contact contact: oraclePhoneBook.search(keyWord)){
			
			System.out.println(contact + ", " + oraclePhoneBook.getCompany(contact));
			
		}
		if(oraclePhoneBook.search(keyWord).isEmpty()){
		System.out.println("No matched contact.");
		return;
		}
	}
	
	/**
	 * Shows the interface of PhonebookApp
	 */
	private static void showInterface(){
		System.out.println("[1] - Show list of contacts");
		System.out.println("[2] - Show a contact.");
		System.out.println("[3] - Search for contacts.");
		System.out.println("[4] - Delete a contact.");
		System.out.println("[5] - Create new contact.");
		System.out.println("[6] - Update contact number.");
		System.out.println("[7] - Exit phonebook app.");
	}
}
