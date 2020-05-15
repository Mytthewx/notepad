package eu.mytthew;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Notebook {
	List<User> users = new ArrayList<>();

	public void addNote(User user) {
		System.out.println("Note title: ");
		Scanner scanner = new Scanner(System.in);
		String title = scanner.nextLine();
		System.out.println("Your note:");
		String content = scanner.nextLine();
		Note note = new Note(title, content);
		for (User searchedUser : users) {
			if (searchedUser.equals(user)) {
				user.addNote(note);
			}
		}
	}

	public String displayNotes(User user) {
		for (User searchedUser : users) {
			if (searchedUser.equals(user)) {
				return user.getNotes();
			}
		}
		return "No notes here.";
	}
}
