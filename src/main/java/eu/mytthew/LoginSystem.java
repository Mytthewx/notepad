package eu.mytthew;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginSystem {
	private final List<User> users = new ArrayList<>();
	private User loggedUser;

	public User getLoggedUser() {
		return loggedUser;
	}

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
				.filter(user -> user.getPassword().equals(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()));
		if (optional.isPresent()) {
			loggedUser = optional.get();
			return true;
		}
		return false;
	}

	public void addUser(String nickname, String password) {
		users.add(new User(nickname, password));
	}
}

