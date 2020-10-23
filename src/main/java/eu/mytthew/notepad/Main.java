package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.FileAuthService;
import eu.mytthew.notepad.auth.IAuthService;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.Reminder;
import eu.mytthew.notepad.entity.User;
import eu.mytthew.notepad.notes.FileNotesService;
import eu.mytthew.notepad.notes.INotesService;
import eu.mytthew.notepad.utils.Config;
import eu.mytthew.notepad.utils.FileOperation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);
	private static final FileOperation fileOperation = new FileOperation("config");
	private static final Config config = new Config(fileOperation);
	private static final INotesService notesService;
	private static final IAuthService authService;

	static {
		config.deserialize(fileOperation.openFile("config"));
		authService = new FileAuthService(new FileOperation("users"), config);
		notesService = new FileNotesService(new FileOperation("notes"), new FileOperation("reminders"), config);
	}


	public static void main(String[] args) {
		List<MenuItem> loginMenuItems = new ArrayList<>();
		loginMenuItems.add(new MenuItem(0, "Exit", () -> false));
		loginMenuItems.add(new MenuItem(1, "Log in", () -> logIn(authService)));
		loginMenuItems.add(new MenuItem(2, "Add user", () -> addUser(authService)));
		boolean repeat = true;
		while (repeat) {
			loginMenuItems
					.stream()
					.map(menu -> menu.getId() + ". " + menu.getName())
					.forEach(System.out::println);
			int selection = scanner.nextInt();
			scanner.nextLine();
			repeat = loginMenuItems.get(selection).getBody().get();
		}
	}

	public static void loginLoop(IAuthService authService, String login) {
		boolean isUserLogged = false;
		while (!isUserLogged) {
			System.out.println("Type password: ");
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
		menuItems.add(new MenuItem(0, "Exit", () -> {
			System.exit(0);
			return false;
		}));
		menuItems.add(new MenuItem(1, "Add note", () -> addNote(authService)));
		menuItems.add(new MenuItem(2, "Review notes", () -> displayAllNotes(authService)));
		menuItems.add(new MenuItem(3, "Display today notes", () -> displayTodayNotes(authService)));
		menuItems.add(new MenuItem(4, "Remove note", () -> removeNotes(authService)));
		menuItems.add(new MenuItem(5, "Edit note", () -> editNote(authService)));
		menuItems.add(new MenuItem(6, "Add reminder", () -> addReminder(authService)));
		menuItems.add(new MenuItem(7, "Edit reminder", () -> editReminder(authService)));
		menuItems.add(new MenuItem(8, "Remove reminder", () -> removeReminder(authService)));
		menuItems.add(new MenuItem(9, "Change login", () -> changeLogin(authService)));
		menuItems.add(new MenuItem(10, "Change password", () -> changePassword(authService)));
		menuItems.add(new MenuItem(11, "Logout", () -> {
			authService.logout();
			System.out.println("Log out.");
			return false;
		}));
		boolean repeat = true;
		while (repeat) {
			menuItems
					.stream()
					.map(menuItem -> menuItem.getId() + ". " + menuItem.getName())
					.forEach(System.out::println);
			int selection = scanner.nextInt();
			scanner.nextLine();
			repeat = menuItems
					.get(selection)
					.getBody()
					.get();
		}
	}

	public static String formatNote(Note note) {
		List<Reminder> reminders = notesService.getAllReminders(note.getId());
		return "\nID: " + note.getId() +
				"\nDate: " + note.getNoteDate() +
				"\nTitle: " + note.getTitle() +
				"\nContent: '" + note.getContent() + '\'' +
				reminders
						.stream()
						.filter(reminder -> reminder.getNoteId() == note.getId())
						.filter(reminder -> reminder.getDate().equals(LocalDate.now()))
						.map(reminder -> "\nReminder ID: " + reminder.getId() +
								"\nReminder name: " + reminder.getName())
						.collect(Collectors.joining()) + "\n";
	}

	public static void logIn(IAuthService authService) {
		System.out.println("Type nickname: ");
		String login = scanner.nextLine();
		if (authService.containsNickname(login)) {
			loginLoop(authService, login);
		} else {
			System.out.println("The user with the given name does not exist.");
		}
	}

	public static void addUser(IAuthService authService) {
		System.out.println("Type nickname: ");
		String nickname = scanner.nextLine();
		System.out.println("Type password: ");
		String password = scanner.nextLine();
		User user = authService.addUser(nickname, password);
		if (user == null) {
			System.out.println("This nickname is already taken.");
		} else {
			config.addUserId(user.getId());
			System.out.println("User added successfully.");
		}
	}

	public static void addNote(IAuthService authService) {
		System.out.println("Note title:");
		String title = scanner.nextLine();
		System.out.println("Your note:");
		String content = scanner.nextLine();
		System.out.println("Set date [yyyy-MM-dd]:");
		String date = scanner.nextLine();
		User loggedUser = authService.getLoggedUser();
		if (date.equals("")) {
			date = String.valueOf(LocalDate.now());
			Note note = new Note(title, content, LocalDate.parse(date));
			Note newNote = notesService.addNote(loggedUser, note);
			config.addNoteId(newNote.getId());
			System.out.println("Note added successfully!");
		} else if (date.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
			Note note = new Note(title, content, LocalDate.parse(date));
			Note newNote = notesService.addNote(loggedUser, note);
			config.addNoteId(newNote.getId());
			System.out.println("Note added successfully!");
		} else {
			System.out.println("Wrong date format.");
		}
	}

	public static void displayAllNotes(IAuthService authService) {
		if (!notesService.userContainsAnyNotes(authService.getLoggedUser())) {
			System.out.println("No notes");
		} else {
			notesService.getAllNotes(authService.getLoggedUser())
					.stream()
					.filter(note -> note.getUserId() == authService.getLoggedUser().getId())
					.map(Main::formatNote)
					.forEach(System.out::println);
		}
	}

	public static void displayTodayNotes(IAuthService authService) {
		if (!notesService.userContainsAnyNotes(authService.getLoggedUser())) {
			System.out.println("No notes.");
		} else {
			notesService.getAllNotes(authService.getLoggedUser())
					.stream()
					.filter(note -> note.getUserId() == authService.getLoggedUser().getId())
					.filter(note -> note.getNoteDate().equals(LocalDate.now()))
					.map(Main::formatNote)
					.forEach(System.out::println);
		}
	}

	public static void removeNotes(IAuthService authService) {
		if (!notesService.userContainsAnyNotes(authService.getLoggedUser())) {
			System.out.println("No notes.");
		} else {
			System.out.println("Select note: ");
			int id = scanner.nextInt();
			scanner.nextLine();
			if (notesService.noteWithThisIdExistAndBelongToUser(id, authService.getLoggedUser())) {
				if (notesService.removeNote(id)) {
					System.out.println("Note removed.");
				}
			} else {
				System.out.println("Wrong note id.");
			}
		}
	}

	public static boolean editNote(IAuthService authService) {
		if (!notesService.userContainsAnyNotes(authService.getLoggedUser())) {
			System.out.println("No notes.");
		} else {
			System.out.println("Select note to edit:");
			int selectedNote = scanner.nextInt();
			scanner.nextLine();
			if (notesService.noteWithThisIdExistAndBelongToUser(selectedNote, authService.getLoggedUser())) {
				System.out.println("New title:");
				String newTitle = scanner.nextLine();
				System.out.println("New content:");
				String newContent = scanner.nextLine();
				System.out.println("New date:");
				String newDate = scanner.nextLine();
				notesService.editNote(selectedNote, newTitle, newContent, newDate);
				System.out.println("Note changed successfully.");
			} else {
				System.out.println("Note with this id doesn't exist.");
			}
		}
		return true;
	}

	public static boolean addReminder(IAuthService authService) {
		if (!notesService.userContainsAnyNotes(authService.getLoggedUser())) {
			System.out.println("No notes.");
			return true;
		}
		System.out.println("Select note:");
		int selectedNote = scanner.nextInt();
		scanner.nextLine();
		if (notesService.noteWithThisIdExistAndBelongToUser(selectedNote, authService.getLoggedUser())) {
			System.out.println("Reminder name:");
			String reminderName = scanner.nextLine();
			System.out.println("Reminder date [yyyy-MM-dd]:");
			String reminderDate = scanner.nextLine();
			Reminder reminder = new Reminder(reminderName, LocalDate.parse(reminderDate));
			Reminder newReminder = notesService.addReminder(selectedNote, reminder);
			config.addReminderId(newReminder.getId());
			System.out.println("Reminder added successfully.");
			return true;
		}
		System.out.println("Wrong note id.");
		return true;
	}

	public static boolean editReminder(IAuthService authService) {
		if (!notesService.userContainsAnyNotes(authService.getLoggedUser())) {
			System.out.println("No notes.");
			return true;
		}
		System.out.println("Select note:");
		int selectedNote = scanner.nextInt();
		scanner.nextLine();
		if (notesService.noteWithThisIdExistAndBelongToUser(selectedNote, authService.getLoggedUser())) {
			if (notesService.noteContainsAnyReminders(selectedNote)) {
				System.out.println("Select reminder:");
				int selectedReminder = scanner.nextInt();
				scanner.nextLine();
				if (notesService.reminderWithThisIdExistAndBelongToNote(selectedReminder, selectedNote)) {
					System.out.println("New reminder name:");
					String newReminderName = scanner.nextLine();
					System.out.println("New reminder date [yyyy-MM-dd]:");
					String newReminderDate = scanner.nextLine();
					notesService.editReminder(selectedReminder, newReminderName, newReminderDate);
					System.out.println("Reminder changed successfully.");
					return true;
				} else {
					System.out.println("Wrong reminder id.");
				}
			} else {
				System.out.println("This note has no reminders.");
				return true;
			}
		}
		System.out.println("Wrong note id.");
		return true;
	}

	public static boolean removeReminder(IAuthService authService) {
		if (!notesService.userContainsAnyNotes(authService.getLoggedUser())) {
			System.out.println("No notes.");
			return true;
		}
		System.out.println("Select note:");
		int selectedNote = scanner.nextInt();
		scanner.nextLine();
		if (notesService.noteWithThisIdExistAndBelongToUser(selectedNote, authService.getLoggedUser())) {
			if (!notesService.noteContainsAnyReminders(selectedNote)) {
				System.out.println("This note has no reminders.");
				return true;
			}
			System.out.println("Select reminder:");
			int selectedReminder = scanner.nextInt();
			scanner.nextLine();
			if (!notesService.reminderWithThisIdExistAndBelongToNote(selectedReminder, selectedNote)) {
				System.out.println("Wrong reminder id.");
				return true;
			}
			if (notesService.removeReminder(selectedNote, selectedReminder)) {
				System.out.println("Reminder removed successfully.");
				return true;
			}
		}
		System.out.println("Wrong note id.");
		return true;
	}

	public static void changeLogin(IAuthService authService) {
		System.out.println("Type new login: ");
		String newNickname = scanner.nextLine();
		if (authService.containsNickname(newNickname)) {
			System.out.println("This nickname is already taken.");
		} else {
			String oldNickname = authService.getLoggedUser().getNickname();
			authService.changeNickname(newNickname);
			System.out.println("Nickname changed successfully!");
			System.out.println("Old nickname: " + oldNickname);
			System.out.println("New nickname: " + newNickname);
		}
	}

	public static void changePassword(IAuthService authService) {
		System.out.println("Type current password:");
		String currentPassword = scanner.nextLine();
		System.out.println("Type new password:");
		String newPassword = scanner.nextLine();
		if (authService.changePassword(currentPassword, newPassword)) {
			System.out.println("Password changed successfully.");
		} else {
			System.out.println("Wrong current password.");
		}
	}

	public static Connection connectToDatabase() {
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/notepad", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
