package com.lotus.kerby.domain;

import java.util.Date;

public class Contact {
	private long id;
	private String name;
	private Date birthday;
	private boolean isVIP = false;
	private Long companyID;
	private String contactNumber;
	
	public Contact(String name, Date birthday, boolean isVIP, Long companyID, String contactNumber) {
		super();
		this.name = name;
		this.birthday = birthday;
		this.isVIP = isVIP;
		this.companyID = companyID;
		this.contactNumber = contactNumber;
	}

	public Contact(long id, String name, Date birthday, boolean isVIP, Long companyID, String contactNumber) {
		this(name, birthday, isVIP, companyID, contactNumber);
		this.id = id;
	}
	
	
	@Override
	public String toString() {
		return  "[Name:" + name + "], [Birthday:"
				+ birthday + "], [isVIP:" + isVIP + "], [Company ID:" + companyID
				+ "], [Contact Number:" + contactNumber + "]";
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public boolean isVIP() {
		return isVIP;
	}
	public void setVIP(boolean isVIP) {
		this.isVIP = isVIP;
	}
	public Long getCompanyID() {
		return companyID;
	}
	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
}
