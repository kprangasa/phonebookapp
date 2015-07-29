package com.lotus.kerby.dao;

import java.util.List;

import com.lotus.kerby.domain.Contact;

public interface PhoneBookApp {
	List<Contact> list();
	Contact show(String name);
	List<Contact> search(String keyword);
	void delete(String contact) ;
	void update(String contact , String newNumber) ;
	void add(String name, String number);
}
