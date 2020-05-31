package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static eu.mytthew.notepad.HashPassword.hashPassword;

public class AuthService {
	private final List<User> users = new ArrayList<>();
	@Getter
	User loggedUser;

	public boolean containsNickname(String nickname) {
		return users
				.stream()
				.anyMatch(user -> user.getNickname().equals(nickname));
	}

	public boolean login(String nickname, String password) {
		Optional<User> optional = users
				.stream()
				.filter(user -> user.getNickname().equals(nickname))
				.findAny()
				.filter(user -> user.getPassword().equals(hashPassword(password)));
		if (optional.isPresent()) {
			loggedUser = optional.get();
			return true;
		}
		return false;
	}

	public void changePassword(String password) {
		loggedUser.setPassword(password);
	}

	public boolean identicalPassword(String password) {
		return getLoggedUser().getPassword().equals(hashPassword(password));
	}

	public boolean addUser(String nickname, String password) {
		if (users.stream().anyMatch(user -> user.getNickname().equals(nickname))) {
			return false;
		}
		users.add(new User(nickname, password));
		return true;
	}
}
