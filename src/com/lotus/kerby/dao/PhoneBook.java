package com.lotus.kerby.dao;

import java.util.List;

import com.lotus.kerby.domain.Contact;

public interface PhoneBook {
	List<Contact> list();
	Contact getByName(String name);
	List<Contact> search(String keyword);
	void delete(Contact contact);
	void update(Contact contact);
	void create(Contact contact);
}
