package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.AuthService;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);
	static boolean isUserLogged = false;

	public static void main(String[] args) {
		AuthService authService = new AuthService();
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
					if (authService.containsNickname(login)) {
						loginLoop(authService, login);
					} else {
						System.out.println("The user with the given name does not exist.");
					}
					break;
				case "2":
					System.out.println("Enter nickname: ");
					String nickname = scanner.nextLine();
					System.out.println("Enter password: ");
					String password = scanner.nextLine();
					if (authService.addUser(nickname, password)) {
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
		}
	}

	public static void loginLoop(AuthService authService, String login) {
		while (!isUserLogged) {
			System.out.println("Enter password: ");
			String password = scanner.nextLine();
			if (authService.login(login, password)) {
				System.out.println("Logged in!");
				isUserLogged = true;
				display(authService);
			} else {
				System.out.println("Wrong password!");
			}
		}
	}

	public static void display(AuthService authService) {
		boolean repeat = true;
		while (repeat) {
			System.out.println("Menu:");
			System.out.println("1. Add note");
			System.out.println("2. Review notes");
			System.out.println("3. Remove note");
			System.out.println("4. Change login");
			System.out.println("5. Change password");
			System.out.println("6. Logout");
			System.out.println("0. Exit");
			String selection = scanner.nextLine();
			switch (selection) {
				case "1":
					System.out.println("Note title: ");
					String title = scanner.nextLine();
					System.out.println("Your note:");
					String content = scanner.nextLine();
					authService.getLoggedUser().addNote(new Note(title, content));
					System.out.println("Note added successfully!");
					break;
				case "2":
					if (authService.getLoggedUser().getNotes().isEmpty()) {
						System.out.println("There is no notes.");
					} else {
						displayNotes(authService.getLoggedUser());
					}
					break;
				case "3":
					if (authService.getLoggedUser().getNotes().isEmpty()) {
						System.out.println("There is no notes.");
					} else {
						System.out.println("Select note: ");
						String id = scanner.nextLine();
						if (authService.getLoggedUser().removeNote(Integer.parseInt(id))) {
							System.out.println("Note removed.");
						} else {
							System.out.println("Can't find note with this id.");
						}
					}
					break;
				case "4":
					System.out.println("Type new login: ");
					String newNickname = scanner.nextLine();
					if (authService.containsNickname(newNickname)) {
						System.out.println("This nickname is already taken.");
					} else {
						String oldNickname = authService.getLoggedUser().getNickname();
						authService.getLoggedUser().setNickname(newNickname);
						System.out.println("Nickname changed successfully!");
						System.out.println("Old nickname: " + oldNickname);
						System.out.println("New nickname: " + newNickname);
					}
					break;
				case "5":
					System.out.println("Type new password: ");
					String newPassword = scanner.nextLine();
					if (authService.identicalPassword(newPassword)) {
						System.out.println("Password must be different from the previous password.");
					} else {
						authService.changePassword(newPassword);
						System.out.println("Password changed successfully!");
					}
					break;
				case "6":
					System.out.println("Logged out.");
					return;
				case "0":
					System.exit(0);
				default:
					System.out.println("Wrong choice.");
					break;
			}
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


