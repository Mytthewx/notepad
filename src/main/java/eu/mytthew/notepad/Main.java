package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.DatabaseAuthService;
import eu.mytthew.notepad.auth.IAuthService;
import eu.mytthew.notepad.auth.NoteService;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);
	private static final NoteService noteService = new NoteService();
	private static final Connection connection = connectToDatabase();
	private static int loggedUserId;
	private static final IAuthService authService = new DatabaseAuthService(connection);

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
		return "\nDate: " + note.getNoteDate() +
				"\nTitle: " + note.getTitle() +
				"\nContent: '" + note.getContent() + '\'' +
				note.getReminders()
						.stream()
						.filter(reminder -> reminder.getDate().equals(LocalDate.now()))
						.map(reminder -> "\nReminder name: " + reminder.getName())
						.collect(Collectors.joining()) + "\n";
	}

	public static void verifyEditNote(Note note, String newTitle, String newContent, String newDate) {
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

	public static void verifyEditReminder(Reminder reminder, String newReminderName, String newReminderDate) {
		if (!newReminderName.equals("")) {
			reminder.setName(newReminderName);
		}
		if (!newReminderDate.equals("")) {
			reminder.setDate(LocalDate.parse(newReminderDate));
		}
	}

	public static boolean userContainsAnyNotes(User user) {
		return user.getNotes().isEmpty();
	}

	public static boolean noteContainsAnyReminder(Note note) {
		return note.getReminders().isEmpty();
	}

	public static boolean noteIdExist(String id, User user) {
		return Integer.parseInt(id) >= user.getNotes().size();
	}

	public static boolean reminderIdExist(String id, Note note) {
		return Integer.parseInt(id) >= note.getReminders().size();
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
		if (authService.addUser(nickname, password)) {
			System.out.println("User added successfully!");
		} else {
			System.out.println("This nickname is already taken.");
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
			loggedUser.addNote(note);
			noteService.addNoteToDatabase(connection, loggedUserId, note);
			System.out.println("Note added successfully!");
		} else if (date.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
			loggedUser.addNote(new Note(title, content, LocalDate.parse(date)));

			System.out.println("Note added successfully!");
		} else {
			System.out.println("Wrong date format.");
		}
	}

	public static void displayAllNotes(IAuthService authService) {
		User user = authService.getLoggedUser();
		if (userContainsAnyNotes(user)) {
			System.out.println("No notes.");
		} else {
			user.getNotes()
					.stream()
					.map(Main::formatNote)
					.forEach(System.out::println);
		}
	}

	public static void displayTodayNotes(IAuthService authService) {
		User user = authService.getLoggedUser();
		if (user.getNotes()
				.stream()
				.noneMatch(note -> note.getNoteDate().equals(LocalDate.now()))) {
			System.out.println("No notes for today.");
		} else {
			user.getNotes()
					.stream()
					.filter(note -> note.getNoteDate().equals(LocalDate.now()))
					.map(Main::formatNote)
					.forEach(System.out::println);
		}
	}

	public static void removeNotes(IAuthService authService) {
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
	}

	public static boolean editNote(IAuthService authService) {
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
				verifyEditNote(user.getNotes().get(Integer.parseInt(selectedNote)), newTitle, newContent, newDate);
				System.out.println("Note changed successfully.");
			} else {
				System.out.println("Note with this id doesn't exist.");
			}
		}
		return true;
	}

	public static boolean addReminder(IAuthService authService) {
		System.out.println("Select note:");
		String selectedNote = scanner.nextLine();
		User user = authService.getLoggedUser();
		if (Integer.parseInt(selectedNote) < user.getNotes().size()) {
			System.out.println("Reminder name:");
			String reminderName = scanner.nextLine();
			System.out.println("Reminder date [yyyy-MM-dd]:");
			String reminderDate = scanner.nextLine();
			user.getNotes().get(Integer.parseInt(selectedNote)).getReminders().add(new Reminder(reminderName, LocalDate.parse(reminderDate)));
			System.out.println("Reminder added successfully.");
		} else {
			System.out.println("Note with this id doesn't exist.");
		}
		return true;
	}

	public static boolean editReminder(IAuthService authService) {
		User user = authService.getLoggedUser();
		if (userContainsAnyNotes(user)) {
			System.out.println("No notes.");
			return true;
		}
		System.out.println("Select note:");
		String selectedNote = scanner.nextLine();
		if (noteIdExist(selectedNote, user)) {
			System.out.println("Note with this id doesn't exist.");
			return true;
		}
		Note note = user.getNotes().get(Integer.parseInt(selectedNote));
		if (noteContainsAnyReminder(note)) {
			System.out.println("This note has no reminder.");
			return true;
		}
		System.out.println("Select reminder:");
		String selectedReminder = scanner.nextLine();
		if (reminderIdExist(selectedReminder, note)) {
			System.out.println("Reminder with this id doesn't exist.");
			return true;
		}
		Reminder reminder = note.getReminders().get(Integer.parseInt(selectedReminder));
		System.out.println("New reminder name:");
		String newReminderName = scanner.nextLine();
		System.out.println("New reminder date [yyyy-MM-dd]:");
		String newReminderDate = scanner.nextLine();
		verifyEditReminder(reminder, newReminderName, newReminderDate);
		System.out.println("Reminder changed successfully.");
		return true;
	}

	public static boolean removeReminder(IAuthService authService) {
		User user = authService.getLoggedUser();
		if (userContainsAnyNotes(user)) {
			System.out.println("No notes.");
			return true;
		}
		System.out.println("Select note:");
		String selectedNote = scanner.nextLine();
		if (noteIdExist(selectedNote, user)) {
			System.out.println("Note with this id doesn't exist.");
			return true;
		}
		Note note = user.getNotes().get(Integer.parseInt(selectedNote));
		if (noteContainsAnyReminder(note)) {
			System.out.println("This note has no reminder.");
			return true;
		}
		System.out.println("Select reminder:");
		String selectedReminder = scanner.nextLine();
		if (reminderIdExist(selectedReminder, note)) {
			System.out.println("Reminder with this id doesn't exist.");
			return true;
		}
		note.getReminders().remove(Integer.parseInt(selectedReminder));
		System.out.println("Reminder removed successfully.");
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

	public int getLoggedUserId(User user) {
		String getUserID = "SELECT id FROM users WHERE login = ?";
		try {
			PreparedStatement getUserIdStatement = connection.prepareStatement(getUserID);
			getUserIdStatement.setString(1, authService.getLoggedUser().getNickname());
			getUserIdStatement.execute();
			getUserIdStatement.getResultSet().next();
			loggedUserId = getUserIdStatement.getResultSet().getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loggedUserId;
	}
}
