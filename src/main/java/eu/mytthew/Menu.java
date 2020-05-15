package eu.mytthew;

import java.util.Scanner;

public class Menu {
	Notes notes = new Notes();
	LoginSystem loginSystem = new LoginSystem();

	public void loginMenu() {
		System.out.println("1. Log in");
		System.out.println("2. Add user");
		System.out.println("0. Exit");
		Scanner scanner = new Scanner(System.in);
		int selection = scanner.nextInt();
		switch (selection) {
			case 1:
				if (loginSystem.login()) {
					display();
				} else {
					loginMenu();
				}
				break;
			case 2:
				loginSystem.addUser();
				loginMenu();
				break;
			case 4:
				return;
			default:
				break;
		}
	}

	public void display() {
		System.out.println("Menu:");
		System.out.println("1. Add note");
		System.out.println("2. Review note");
		System.out.println("3. Remove note");
		System.out.println("0. Exit");
		Scanner scanner = new Scanner(System.in);
		int selection = scanner.nextInt();
		switch (selection) {
			case 1:
				notes.addNote();
				display();
				break;
			case 2:
				notes.displayNote();
				break;
			default:
				break;
		}
	}
}
