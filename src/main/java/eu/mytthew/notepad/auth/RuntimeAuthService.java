package eu.mytthew.notepad.auth;

import com.google.common.hash.Hashing;
import eu.mytthew.notepad.entity.User;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	public boolean addUser(String nickname, String password) {
		if (users.stream().anyMatch(user -> user.getNickname().equals(nickname))) {
			return false;
		}
		int id = userProvider.next();
		User user = new User(id, nickname, hashPassword(password));
		users.add(user);
		return true;
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

	private String hashPassword(String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
