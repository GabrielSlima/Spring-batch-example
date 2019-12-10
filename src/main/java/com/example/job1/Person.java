package com.example.job1;

public class Person {
	String name;
	String lastName;
	
	public Person() {
	  }
	
	public Person(String name, String lastName) {
		this.name = name;
		this.lastName = lastName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	 @Override
	  public String toString() {
	    return "firstName: " + name + ", lastName: " + lastName;
	  }
}
