package com.huawei.cloud.entity;

public class User {
	private long id;
	private String name;
	private String password;
	
	public User(){
		
	}
	
	public User(long id,String name){
		this.id = id;
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
}

