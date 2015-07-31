package com.lotus.kerby.domain;

public class Company {
	private long id;
	private String name;
	private String code;
	private String description;
	
	public Company(String name, String code, String description) {
		super();
		this.name = name;
		this.code = code;
		this.description = description;
	}
	
	public Company(long id, String name, String code, String description) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void persist(){
		
	}

	@Override
	public String toString() {
		return "[Company name:" + name + "], [Company description:" + description + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
