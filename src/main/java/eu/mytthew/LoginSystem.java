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

	public boolean containsNickname(String nickname) {
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

	public void addUser(String nickname, String password) {
		users.add(new User(nickname, password));
	}

	public void changeNickname(String newNickname) {
		loggedUser.setNickname(newNickname);
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
