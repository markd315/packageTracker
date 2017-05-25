package packageTracker;

import java.util.ArrayList;

public class Person {
	static ArrayList<Person> people = new ArrayList<Person>();
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAptNumber() {
		return aptNumber;
	}

	public void setAptNumber(int aptNumber) {
		this.aptNumber = aptNumber;
	}

	public static ArrayList<Person> getPeople() {
		return people;
	}

	private String firstname, lastname, email;
	private int aptNumber;

	public Person(int apt, String fn, String ln, String email) {
		this.aptNumber = apt;
		this.firstname = fn;
		this.lastname = ln;
		this.email = email;
		people.add(this);
	}
}
