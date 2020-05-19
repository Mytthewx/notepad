package eu.mytthew;

import java.util.Scanner;

public class Main {


	public static void main(String[] args) {
		LoginSystem loginSystem = new LoginSystem();
		User loggedUser = null;
		boolean isUserLogged = false;
		boolean repeat = true;
		while (repeat) {
			System.out.println("1. Log in");
			System.out.println("2. Add user");
			System.out.println("0. Exit");
			Scanner scanner = new Scanner(System.in);
			String selection = scanner.next();
			switch (selection) {
				case "1":
					Scanner loginScanner = new Scanner(System.in);
					System.out.println("Enter nickname: ");
					String login = loginScanner.next();
					if (loginSystem.containsNickname(login)) {
						while (!isUserLogged) {
							System.out.println("Enter password: ");
							String password = loginScanner.next();
							if (loginSystem.login(login, password)) {
								isUserLogged = true;
								loggedUser = loginSystem.getLoggedUser();
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
					Scanner addUserScanner = new Scanner(System.in);
					System.out.println("Enter nickname: ");
					String nickname = addUserScanner.next();
					if (!loginSystem.containsNickname(nickname)) {
						System.out.println("Enter password: ");
						String password = addUserScanner.next();
						loginSystem.addUser(nickname, password);
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
				display(loginSystem, loggedUser);
			}
		}
	}

	public static void display(LoginSystem loginSystem, User loggedUser) {
		System.out.println("Menu:");
		System.out.println("1. Add note");
		System.out.println("2. Review notes");
		System.out.println("3. Remove note");
		System.out.println("4. Change login");
		System.out.println("5. Change password");
		System.out.println("0. Exit");
		Scanner scanner = new Scanner(System.in);
		int selection = scanner.nextInt();
		switch (selection) {
			case 1:
				addNote(loggedUser);
				break;
			case 2:
				if (loggedUser.getNotes().isEmpty()) {
					System.out.println("There is no notes.");
				} else {
					loggedUser.getNotes().stream().map(Note::toString).forEach(System.out::println);
				}
				break;
			case 3:
				if (loggedUser.getNotes().isEmpty()) {
					System.out.println("There is no notes.");
				} else {
					System.out.println("Select note: ");
					int id = scanner.nextInt();
					if (loggedUser.removeNote(id)) {
						System.out.println("Note removed.");
					} else {
						System.out.println("Can't find note with this id.");
					}
				}
				break;
			case 4:
				loginSystem.changeNickname();
				break;
			case 5:
				loginSystem.changePassword();
				break;
			case 0:
				System.exit(0);
			default:
				break;
		}
	}

	public static void addNote(User user) {
		System.out.println("Note title: ");
		Scanner scanner = new Scanner(System.in);
		String title = scanner.nextLine();
		System.out.println("Your note:");
		String content = scanner.nextLine();
		Note note = new Note(title, content);
		user.addNote(note);
		System.out.println("Note added successfully!");
	}
}


