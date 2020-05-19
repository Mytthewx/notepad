package eu.mytthew;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginSystem {
	private List<User> users = new ArrayList<>();
	private User loggedUser;

	public List<User> getUsers() {
		return users;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public boolean contains(String nickname) {
		return users.stream().anyMatch(user -> user.getNickname().equals(nickname));
	}

	public boolean login(String nickname, String password) {
		for (User user : users) {
			if (user.getNickname().equals(nickname)) {
				if (user.getPassword().equals(password)) {
					loggedUser = user;
					return true;
				}
			}
		}
		return false;
	}

	public void addUser() {
		Scanner scanner = new Scanner(System.in);
		String nickname;
		String password;
		System.out.print("Enter nickname: ");
		nickname = scanner.next();
		System.out.print("Enter password: ");
		password = scanner.next();
		users.add(new User(nickname, password));
		System.out.println("User added successfully!");
	}

	public void changeNickname() {
		String oldNickname = loggedUser.getNickname();
		System.out.println("Type new login: ");
		Scanner nicknameScanner = new Scanner(System.in);
		String nickname = nicknameScanner.nextLine();
		for (User user : users) {
			if (user.getNickname().equals(nickname)) {
				System.out.println("This nickname is already taken.");
			} else {
				loggedUser.setNickname(nickname);
				System.out.println("Old nickname: " + oldNickname);
				System.out.println("New nickname: " + loggedUser.getNickname());
			}
		}
	}

	public void changePassword() {
		System.out.println("Type new password: ");
		Scanner passwordScanner = new Scanner(System.in);
		String password = passwordScanner.nextLine();
		for (User user : users) {
			if (user.getPassword().equals(password)) {
				System.out.println("Password must be different from the previous password.");
			} else {
				loggedUser.setPassword(password);
			}
		}
	}
}
