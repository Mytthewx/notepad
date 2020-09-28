package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static eu.mytthew.notepad.auth.HashPassword.hashPassword;

public class RuntimeAuthService implements IAuthService {
	private final List<User> users = new ArrayList<>();
	private final IdProvider userProvider = new IdProvider();
	@Getter
	private User loggedUser;

	@Override
	public boolean containsNickname(String nickname) {
		return users
				.stream()
				.anyMatch(user -> user.getNickname().equals(nickname));
	}

	@Override
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

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		if (loggedUser.getPassword().equals(hashPassword(oldPassword))) {
			loggedUser.setPassword(hashPassword(newPassword));
			return true;
		}
		return false;
	}

	@Override
	public User addUser(String nickname, String password) {
		if (users.stream().anyMatch(user -> user.getNickname().equals(nickname))) {
			return null;
		}
		User user = new User(userProvider.next(), nickname, hashPassword(password));
		users.add(user);
		return user;
	}

	@Override
	public boolean logout() {
		loggedUser = null;
		return true;
	}

	@Override
	public boolean changeNickname(String newNickname) {
		if (containsNickname(newNickname)) {
			return false;
		}
		getLoggedUser().setNickname(newNickname);
		return true;
	}
}
