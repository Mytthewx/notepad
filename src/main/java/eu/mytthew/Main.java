package eu.mytthew;

import java.util.Scanner;

public class Main {
	static final Notebook notebook = new Notebook();
	static final LoginSystem loginSystem = new LoginSystem();
	static User loggedUser;

	public static void main(String[] args) {
		while (true) {
			System.out.println("1. Log in");
			System.out.println("2. Add user");
			System.out.println("0. Exit");
			Scanner scanner = new Scanner(System.in);
			int selection = scanner.nextInt();
			switch (selection) {
				case 1:
					loginSystem.login();
					loggedUser = loginSystem.getLoggedUser();
					while (true) {
						display();
					}
				case 2:
					loginSystem.addUser();
					break;
				case 0:
					return;
				default:
					break;
			}
		}
	}

	public static void display() {
		System.out.println("Menu:");
		System.out.println("1. Add note");
		System.out.println("2. Review note");
		System.out.println("3. Remove note");
		System.out.println("0. Exit");
		Scanner scanner = new Scanner(System.in);
		int selection = scanner.nextInt();
		switch (selection) {
			case 1:
				notebook.addNote(loggedUser);
				break;
			case 2:
				notebook.displayNotes(loggedUser);
				break;
			default:
				break;
		}
	}
}


