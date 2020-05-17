package eu.mytthew;

import java.util.Scanner;

public class Main {


	public static void main(String[] args) {
		Notebook notebook = new Notebook();
		LoginSystem loginSystem = new LoginSystem();
		User loggedUser;
		boolean repeat = true;
		boolean repeatInside = true;
		while (repeat) {
			System.out.println("1. Log in");
			System.out.println("2. Add user");
			System.out.println("0. Exit");
			Scanner scanner = new Scanner(System.in);
			int selection = scanner.nextInt();
			switch (selection) {
				case 1:
					loginSystem.login();
					loggedUser = loginSystem.getLoggedUser();
					while (repeatInside) {
						display(notebook, loggedUser);
					}
					break;
				case 2:
					loginSystem.addUser();
					break;
				case 0:
					repeat = false;
					return;
				default:
					break;
			}
		}
	}

	public static void display(Notebook notebook, User loggedUser) {
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
			case 0:
				break;
			default:
				break;
		}
	}
}


