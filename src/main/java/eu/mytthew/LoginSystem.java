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
		boolean correctPassword = false;
		Scanner scanner = new Scanner(System.in);
		String nickname;
		String password;
		System.out.print("Enter nickname: ");
		nickname = scanner.next();
		for (User user : users) {
			if (user.getNickname().equals(nickname)) {
				while (!correctPassword) {
					System.out.print("Enter password: ");
					password = scanner.next();
					if (user.getPassword().equals(password)) {
						System.out.println("Logged in.");
						loggedUser = user;
						correctPassword = true;
					} else {
						System.out.println("Wrong password.");
					}
				}
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

}
