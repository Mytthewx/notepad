package eu.mytthew;

import java.util.Scanner;

public class Notebook {
	public void addNote(User user) {
		System.out.println("Note title: ");
		Scanner scanner = new Scanner(System.in);
		String title = scanner.nextLine();
		System.out.println("Your note:");
		String content = scanner.nextLine();
		Note note = new Note(title, content);
		user.addNote(note);
		System.out.println("Note added successfully!");
	}

	public void displayNotes(User user) {
		System.out.println(user.getNotes());
	}
}
