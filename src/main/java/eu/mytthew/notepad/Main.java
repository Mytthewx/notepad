package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.IAuthService;
import eu.mytthew.notepad.auth.RuntimeAuthService;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		IAuthService authService = new RuntimeAuthService();
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

	public static void loginLoop(IAuthService authService, String login) {
		boolean isUserLogged = false;
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

	public static void display(IAuthService authService) {
		List<MenuItem> menuItems = new ArrayList<>();
		menuItems.add(new MenuItem(0, "0. Exit", () -> {
			System.exit(0);
			return false;
		}));
		menuItems.add(new MenuItem(1, "1. Add note", () -> {
			System.out.println("Note title:");
			String title = scanner.nextLine();
			System.out.println("Your note:");
			String content = scanner.nextLine();
			System.out.println("Set date [yyyy-MM-dd]:");
			String date = scanner.nextLine();
			User loggedUser = authService.getLoggedUser();
			if (date.equals("")) {
				date = String.valueOf(LocalDate.now());
				loggedUser.addNote(new Note(title, content, LocalDate.parse(date)));
				System.out.println("Note added successfully!");
			} else if (date.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
				loggedUser.addNote(new Note(title, content, LocalDate.parse(date)));
				System.out.println("Note added successfully!");
			} else {
				System.out.println("Wrong date format.");
				return false;
			}
			return true;
		}));
		menuItems.add(new MenuItem(2, "2. Review notes", () -> {
			if (authService.getLoggedUser().getNotes().isEmpty()) {
				System.out.println("No notes.");
			} else {
				displayNotes(authService.getLoggedUser());
			}
			return true;
		}));
		menuItems.add(new MenuItem(3, "3. Remove note", () -> {
			if (authService.getLoggedUser().getNotes().isEmpty()) {
				System.out.println("No notes.");
			} else {
				System.out.println("Select note: ");
				String id = scanner.nextLine();
				User user = authService.getLoggedUser();
				if (user.getNotes().size() > Integer.parseInt(id)) {
					if (user.removeNote(user.getNotes().get(Integer.parseInt(id)).getUuid())) {
						System.out.println("Note removed.");
					}
				} else {
					System.out.println("Note with this id doesn't exist.");
				}
			}
			return true;
		}));
		menuItems.add(new MenuItem(4, "4. Edit note", () -> {
			if (authService.getLoggedUser().getNotes().isEmpty()) {
				System.out.println("No notes.");
			} else {
				System.out.println("Select note to edit:");
				String selectedNote = scanner.nextLine();
				User user = authService.getLoggedUser();
				if (Integer.parseInt(selectedNote) < user.getNotes().size()) {
					System.out.println("New title:");
					String newTitle = scanner.nextLine();
					System.out.println("New content:");
					String newContent = scanner.nextLine();
					System.out.println("New date:");
					String newDate = scanner.nextLine();
					editNote(user.getNotes().get(Integer.parseInt(selectedNote)), newTitle, newContent, newDate);
					System.out.println("Note changed successfully.");
				} else {
					System.out.println("Note with this id doesn't exist.");
					return false;
				}
			}
			return true;
		}));
		menuItems.add(new MenuItem(5, "5. Change login", () -> {
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
			return true;
		}));
		menuItems.add(new MenuItem(6, "6. Change password", () -> {
			System.out.println("Type current password:");
			String currentPassword = scanner.nextLine();
			System.out.println("Type new password:");
			String newPassword = scanner.nextLine();
			if (authService.changePassword(currentPassword, newPassword)) {
				System.out.println("Password changed successfully.");
			} else {
				System.out.println("Wrong current password.");
			}
			return true;
		}));
		menuItems.add(new MenuItem(7, "7. Logout", () -> {
			System.out.println("Logged out.");
			return false;
		}));
		boolean repeat = true;
		while (repeat) {
			menuItems.stream().map(MenuItem::getName).forEach(System.out::println);
			int selection = scanner.nextInt();
			scanner.nextLine();
			repeat = menuItems.get(selection).getBody().get();
		}
	}

	public static void displayNotes(User user) {
		user.getNotes().stream().map(note ->
				"ID: " + user.getNotes().indexOf(note) +
						"\nDate: " + note.getNoteDate() +
						"\nTitle: " + note.getTitle() +
						"\nContent: '" + note.getContent() + '\'' + "\n")
				.forEach(System.out::println);
	}

	public static void editNote(Note note, String newTitle, String newContent, String newDate) {
		if (!newTitle.equals("")) {
			note.setTitle(newTitle);
		}
		if (!newContent.equals("")) {
			note.setContent(newContent);
		}
		if (!newDate.equals("")) {
			note.setNoteDate(LocalDate.parse(newDate));
		}
	}
}


