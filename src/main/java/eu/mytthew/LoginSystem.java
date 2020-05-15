package eu.mytthew;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoginSystem {
	public void login() throws FileNotFoundException {
		Scanner content = new Scanner(new File("content.txt"));
		Scanner keyboard = new Scanner(System.in);
		String user = content.nextLine();
		String password = content.nextLine();

		String inputUser = keyboard.nextLine();
		String inputPassword = keyboard.nextLine();

		if (inputUser.equals(user) && inputPassword.equals(password)) {
			System.out.println("Logged successfully.");
		} else {
			System.out.println("Error.");
		}
	}

}
