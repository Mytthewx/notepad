package eu.mytthew;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginSystem {
	List<User> users = new ArrayList<>();
	User loggedUser;

	public User getLoggedUser() {
		return loggedUser;
	}


	public void login() {
		Scanner scanner = new Scanner(System.in);
		String nickname;
		String password;
		System.out.print("Enter nickname: ");
		nickname = scanner.next();
		for (User user : users) {
			if (user.getNickname().equals(nickname)) {
				System.out.print("Enter password: ");
				password = scanner.next();
				if (user.getPassword().equals(password)) {
					System.out.println("Logged in.");
					loggedUser = user;
				} else {
					System.out.println("Wrong password.");
				}
				return;
			} else {
				System.out.println("I can't find user with this nickname.");
			}
		}
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

}
