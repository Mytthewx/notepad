package eu.mytthew;

import java.util.Scanner;

public class Main {


	public static void main(String[] args) {
		Notebook notebook = new Notebook();
		LoginSystem loginSystem = new LoginSystem();
		User loggedUser = null;
		boolean isUserLogged = false;
		boolean repeat = true;
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
					isUserLogged = true;
					repeat = false;
					break;
				case 2:
					loginSystem.addUser();
					break;
				case 0:
				default:
					repeat = false;
					break;

			}
			while (isUserLogged) {
				display(notebook, loggedUser);
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
			case 3:
				System.out.println("Select note: ");
				int ID = scanner.nextInt();
				notebook.removeNote(loggedUser, ID);
				break;
			case 0:
			default:
				break;
		}
	}
}


