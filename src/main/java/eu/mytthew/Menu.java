package eu.mytthew;

import java.util.Scanner;

public class Menu {
	public static void display() {
		System.out.println("Menu:");
		System.out.println("1. Dodaj notatke");
		System.out.println("2. Przejrzyj notatki");
		System.out.println("3. Usun notatke");
		System.out.println("4. Dodaj uzytkownika");
		System.out.println("5. Usun uzytkownika");
	}

	public static void makeChoice() {
		Scanner choice = new Scanner(System.in);
		int selection;
		selection = choice.nextInt();
		System.out.println("Your selection: " + selection);
	}
}
