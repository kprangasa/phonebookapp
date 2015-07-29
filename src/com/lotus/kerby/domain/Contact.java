package com.lotus.kerby.domain;

import java.util.Date;

public class Contact {
	private long id;
	private String name;
	private Date birthday;
	private boolean isVIP = false;
	private long companyID;
	
	public Contact(String name, Date birthday, boolean isVIP, long companyID) {
		super();
		this.name = name;
		this.birthday = birthday;
		this.isVIP = isVIP;
		this.companyID = companyID;
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
	public long getCompanyID() {
		return companyID;
	}
	public void setCompanyID(long companyID) {
		this.companyID = companyID;
	}
	
}
