package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import eu.mytthew.notepad.utils.Config;
import eu.mytthew.notepad.utils.FileOperation;
import eu.mytthew.notepad.utils.IdProvider;
import lombok.Getter;
import org.json.JSONObject;

import static eu.mytthew.notepad.auth.HashPassword.hashPassword;

public class FileAuthService implements IAuthService {
	private final Config config;
	private final IdProvider userProvider;
	private final FileOperation file;
	@Getter
	private User loggedUser;

	public FileAuthService(FileOperation file, Config config) {
		this.file = file;
		this.config = config;
		userProvider = new IdProvider(config.getUserId());
	}

	@Override
	public boolean containsNickname(String nickname) {
		return file.fileExist(nickname);
	}

	@Override
	public boolean login(String nickname, String password) {
		User user = new User(0, "", "");
		JSONObject obj = file.openFile(nickname);
		user.deserialize(obj);
		if (!user.getPassword().equals(hashPassword(password))) {
			return false;
		}
		loggedUser = user;
		return true;
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		if (loggedUser.getPassword().equals(hashPassword(oldPassword))) {
			JSONObject jsonObject = file.openFile(loggedUser.getNickname());
			String nickname = jsonObject.getString("nick");
			jsonObject.put("nick", nickname);
			jsonObject.put("pass", hashPassword(newPassword));
			file.deleteFile(nickname);
			file.createFile(nickname, jsonObject);
			loggedUser.setPassword(hashPassword(newPassword));
			return true;
		}
		return false;
	}

	@Override
	public boolean changeNickname(String newNickname) {
		if (containsNickname(newNickname)) {
			return false;
		}
		JSONObject jsonObject = file.openFile(loggedUser.getNickname());
		String password = jsonObject.getString("pass");
		jsonObject.put("nick", newNickname);
		jsonObject.put("pass", password);
		file.createFile(newNickname, jsonObject);
		file.deleteFile(loggedUser.getNickname());
		getLoggedUser().setNickname(newNickname);
		return true;
	}

	@Override
	public User addUser(String nickname, String password) {
		User user = new User(userProvider.next(), nickname, hashPassword(password));
		file.createFile(nickname, user.serialize());
		return user;
	}

	@Override
	public boolean logout() {
		if (loggedUser == null) {
			return false;
		}
		loggedUser = null;
		return true;
	}
}
