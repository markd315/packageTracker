package packageTracker;

import java.util.ArrayList;
import java.util.Date;

public class Package {
	static ArrayList<Package> packages = new ArrayList<Package>();
	public static ArrayList<Package> getPackages() {
		return packages;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Date getDate() {
		return date;
	}

	private String firstname, lastname;
	private Date date;

	public Package(String fn, String ln) {
		firstname = fn;
		lastname = ln;
		date = new Date();
		packages.add(this);
	}
	public String verbose(){
		return firstname+ " " +lastname+" has a package from "+date.toString()+" which has been marked as claimed.";
	}
	public String toString(){
		return firstname+" "+lastname+" "+date.toString();
	}
}
