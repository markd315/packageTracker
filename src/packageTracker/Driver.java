package packageTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("names.txt");
		Scanner inFile = new Scanner(file);
		while (inFile.hasNext()) {
			int apt = Integer.parseInt(inFile.nextLine());
			while (true) {
				String stringput;
				try {
					stringput = inFile.nextLine();
				} catch (Exception e) {
					break;
				}
				if (stringput.equalsIgnoreCase("")) {
					break;
				}
				String personData = stringput;
				String[] parts = personData.split(" ");
				new Person(apt, parts[0].substring(1), parts[1], parts[2]);
			}
		}
		inFile.close();
		File pk = new File("packages.txt");
		Scanner pkgs = new Scanner(pk);
		while (pkgs.hasNextLine()) {
			String packageData = pkgs.nextLine();
			String[] parts = packageData.split(" ");
			new Package(parts[0], parts[1]);
		}
		pkgs.close();
		// Data portion complete.

		System.out.println("Loaded existing package and resident info");
		int input = 0;
		while (input != 3) {
			System.out.println("1) New shipment obtained");
			System.out.println("2) Mark package signed for and recieved");
			System.out.println("3) Exit PackageTrack");
			input = 0;
			Scanner is = new Scanner(System.in);
			while (input > 3 || input < 1) {
				input = is.nextInt();
			}
			String stringput = null;
			switch (input) {
			case 1:
				is.nextLine(); // May be needed to advance cursor.
				while (stringput==null || !stringput.equalsIgnoreCase("stop")) {
					System.out.println("Enter STOP to return to main menu.");
					System.out.println("Enter recipient first and last name.");
					stringput = null;
					while (stringput == null) {
						stringput = is.nextLine();
					}
					if (!stringput.equalsIgnoreCase("stop")) {
						String[] parts = stringput.split(" ");
						Package x = new Package(parts[0], parts[1]);
						try {
						    Files.write(Paths.get("packages.txt"), x.toString().getBytes(), StandardOpenOption.APPEND);
						}catch (IOException e) {

						}
						emailRecipient(x);

					}
				}
				break;
			case 2:
				is.nextLine(); // May be needed to advance cursor.
				System.out
						.println("Enter first and last name (seperated by a space).");
				stringput = null;
				while (stringput == null) {
					stringput = is.nextLine();
				}
				Person x = lookupPerson(stringput);
				if (x != null) {
					printAndClearPackages(x);
				}
				break;
			default:
				break;
			}
			is.close();
		}
	}

	private static void printAndClearPackages(Person x)
			throws FileNotFoundException {
		String first = x.getFirstname();
		String last = x.getLastname();
		ArrayList<Package> list = Package.getPackages();
		for (Package h : list) {
			if (first.equalsIgnoreCase(h.getFirstname())
					&& last.equalsIgnoreCase(h.getLastname())) {
				System.out.println(h.verbose());
				list.remove(h);
			}
		}
		updatePackageFile();

	}

	private static Person lookupPerson(String stringput) {
		String[] parts = stringput.split(" ");
		String first = parts[0];
		String last = parts[1];
		ArrayList<Person> list = Person.getPeople();
		for (Person p : list) {
			if (first.equalsIgnoreCase(p.getFirstname())
					&& last.equalsIgnoreCase(p.getLastname()))
				return p;
		}
		System.out.println("Currently no resident by that name, so no email sent or packages found.");
		return null;
	}

	private static void updatePackageFile() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File("packages"));
		ArrayList<Package> list = Package.getPackages();
		for (Package h : list) {
			writer.print(h);
		}
		writer.close();
	}

	private static void emailRecipient(Package x) {
		Person p = lookupPerson(x.toString());
		String[] recipients = {p.getEmail()};
		sendFromGMail("courtyardsPackageAlert", "iqaJ528FEX{}23", recipients, "Package Alert", p.getFirstname()+" has a new package, come to the leasing office Monday through Friday between 9AM and 6PM.");
		System.out.println("Alert sent and package recorded!");
	}

	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
