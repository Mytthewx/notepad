package eu.mytthew;

import java.util.Scanner;

public class Notes {
	private String title;
	private String content;

	public void addNote() {
		System.out.println("Note title: ");
		Scanner scanner = new Scanner(System.in);
		title = scanner.nextLine();
		System.out.println("Your note:");
		content = scanner.nextLine();
	}

	public void displayNote() {
		System.out.println("Title: " + title);
		System.out.print("Your note:\n" + content);
	}
}
