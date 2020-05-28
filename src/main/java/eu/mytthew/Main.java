package eu.mytthew;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static eu.mytthew.HashPassword.hashPassword;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		LoginSystem loginSystem = new LoginSystem();
		boolean isUserLogged = false;
		boolean repeat = true;
		while (repeat) {
			System.out.println("1. Log in");
			System.out.println("2. Add user");
			System.out.println("0. Exit");
			String selection = scanner.nextLine();
			switch (selection) {
				case "1":
					System.out.println("Enter nickname: ");
					String login = scanner.nextLine();
					if (loginSystem.containsNickname(login)) {
						while (!isUserLogged) {
							System.out.println("Enter password: ");
							String password = scanner.nextLine();
							if (loginSystem.login(login, password)) {
								isUserLogged = true;
								repeat = false;
								System.out.println("Logged in!");
							} else {
								System.out.println("Wrong password!");
							}
						}
					} else {
						System.out.println("The user with the given name does not exist.");
					}
					break;
				case "2":
					System.out.println("Enter nickname: ");
					String nickname = scanner.nextLine();
					System.out.println("Enter password: ");
					String password = scanner.nextLine();
					if (loginSystem.addUser(nickname, password)) {
						System.out.println("User added successfully!");
					} else {
						System.out.println("This nickname is already taken.");
					}
					break;
				case "0":
					repeat = false;
					break;
				default:
					System.out.println("Wrong choice. Choose 1, 2 or 0.");
					break;
			}
			while (isUserLogged) {
				display(loginSystem);
			}
		}
	}

	public static void display(LoginSystem loginSystem) {
		System.out.println("Menu:");
		System.out.println("1. Add note");
		System.out.println("2. Review notes");
		System.out.println("3. Remove note");
		System.out.println("4. Change login");
		System.out.println("5. Change password");
		System.out.println("0. Exit");
		String selection = scanner.nextLine();
		switch (selection) {
			case "1":
				System.out.println("Note title: ");
				String title = scanner.nextLine();
				System.out.println("Your note:");
				String content = scanner.nextLine();
				loginSystem.getLoggedUser().addNote(new Note(title, content));
				System.out.println("Note added successfully!");
				break;
			case "2":
				if (loginSystem.getLoggedUser().getNotes().isEmpty()) {
					System.out.println("There is no notes.");
				} else {
					displayNotes(loginSystem.getLoggedUser());
				}
				break;
			case "3":
				if (loginSystem.getLoggedUser().getNotes().isEmpty()) {
					System.out.println("There is no notes.");
				} else {
					System.out.println("Select note: ");
					String id = scanner.nextLine();
					if (loginSystem.getLoggedUser().removeNote(Integer.parseInt(id))) {
						System.out.println("Note removed.");
					} else {
						System.out.println("Can't find note with this id.");
					}
				}
				break;
			case "4":
				System.out.println("Type new login: ");
				String newNickname = scanner.nextLine();
				if (loginSystem.containsNickname(newNickname)) {
					System.out.println("This nickname is already taken.");
				} else {
					String oldNickname = loginSystem.getLoggedUser().getNickname();
					loginSystem.getLoggedUser().setNickname(newNickname);
					System.out.println("Nickname changed successfully!");
					System.out.println("Old nickname: " + oldNickname);
					System.out.println("New nickname: " + newNickname);
				}
				break;
			case "5":
				String oldPassword = loginSystem.getLoggedUser().getPassword();
				System.out.println("Type new password: ");
				String newPassword = scanner.nextLine();
				if (oldPassword.equals(hashPassword(newPassword))) {
					System.out.println("Password must be different from the previous password.");
				} else {
					loginSystem.getLoggedUser().setPassword(newPassword);
					System.out.println("Password changed successfully!");
				}
				break;
			case "0":
				System.exit(0);
			default:
				System.out.println("Wrong choice.");
				break;
		}
	}

	public static void displayNotes(User user) {
		user.getNotes().stream().map(note ->
				"ID: " + note.getId() +
						"\nDate: " + note.getNoteTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
						"\nTitle: " + note.getTitle() +
						"\nContent: '" + note.getContent() + '\'' + "\n")
				.forEach(System.out::println);
	}
}


